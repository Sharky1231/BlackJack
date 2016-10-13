package Common.Game;

import Common.EventType;
import Common.Messages.MessageWrapper;
import Server.ClientCommunicationManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Game {
    private ArrayList<Player> players;
    private Player dealer;
    private Player currentPlayer;
    private CardPack cardPack;
    private Timer timer;
    private boolean gameInProgress;
    private static final int GAME_WAITING_TIME = 8 * 1000;
    private static final int PLAYER_WAITING_TIME = 4 * 1000;

    private static Game game;

    public static Game getInstance() throws IOException {
        if (game == null)
            game = new Game();
        return game;
    }

    private Game() throws IOException {
        this.players = new ArrayList<>();
        this.cardPack = new CardPack();
        this.gameInProgress = false;
        this.dealer = new Player(UUID.randomUUID(), 1500);
        this.currentPlayer = null;
        addPlayer(dealer);
    }

    public void addPlayer(Player player) throws IOException {
        players.add(player);
        notifyConcretePlayer(player.getId(), "Your money: " + player.getMoney());
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean playerWon(Player player) {
        if (player.getId() != dealer.getId()) {
            int playerScore = player.getScore().getPoints();
            int dealerScore = dealer.getScore().getPoints();

            return playerScore > dealerScore && playerScore <= 21;
        }
        return false;
    }

    public void decideWinners() throws IOException {
        for (Player player : players) {
            if (playerWon(player)) {
                player.increaseMoney(player.getBet() * 2);
                dealer.increaseMoney(-player.getBet());
                notifyConcretePlayer(player.getId(), "Congratulations! You won " + (player.getBet() * 2) + ". Your money: " + player.getMoney());
            } else {
                player.increaseMoney(-player.getBet());
                dealer.increaseMoney(player.getBet());
                notifyConcretePlayer(player.getId(), "Sorry you lost. Try again." + "Your money: " + player.getMoney());
            }
        }
    }


    public void dealCardToPlayer(Player player) {
        Card randomCard = cardPack.getRandomCard();
        player.putCardIntoHand(randomCard);
    }

    public void startGame() throws IOException {
        startGameTimer();
    }

    private void startGameTimer() throws IOException {
//        notifyPlayers("----Game starts----");
        this.timer = new Timer("GameTimer");
        timer.scheduleAtFixedRate(setupGameTimer(), GAME_WAITING_TIME, GAME_WAITING_TIME);
    }

    private void startRound() throws InterruptedException, IOException {
        this.timer = new Timer("PlayerTimer");
        gameInProgress = true;

        // Loop through the players and wait for their decision.
        // If they do not decide within time, it will be decided for them. They automatically "Stand"
        for (Player player : players) {
            currentPlayer = player;
            dealCardToPlayer(currentPlayer);
            notifyConcretePlayer(currentPlayer.getId(), showCards());
            waitForResponse();
        }

        // If dealer points are below 16, he draws one more card
        if (dealer.getScore().getPoints() < 16)
            dealCardToPlayer(dealer);

        // Decide winners and give them winnings
        decideWinners();

        gameInProgress = false;

        // Reset players cards
        resetHands();

        // Broadcast game is finished
        notifyPlayersGameFinished();

        startGame();
    }

    private void notifyPlayersGameFinished() throws IOException {
        for (Player player : players) {
            notifyConcretePlayer(player.getId(), EventType.GAME_FINISHED, "----Game ended----\n--Place your bets--\n(Game starts in 8 seconds)");
        }
    }

    private void resetHands() {
        for (Player player : players) {
            player.resetCards();
            player.resetPonts();
        }
    }

    private void waitForResponse() throws InterruptedException {
        timer = new Timer(String.valueOf(currentPlayer.getId()));
        timer.scheduleAtFixedRate(setupPlayerTimer(), PLAYER_WAITING_TIME, PLAYER_WAITING_TIME);
        while (!currentPlayer.responded()) {
            Thread.sleep(500);
        }

        currentPlayer.setResponded(false);
        cancelCurrentTimer();
    }

    private TimerTask setupPlayerTimer() {
        return new TimerTask() {
            @Override
            public void run() {
                stand(currentPlayer.getId());
            }
        };
    }

    private TimerTask setupGameTimer() {
        return new TimerTask() {

            @Override
            public void run() {
                try {
                    if (players.size() > 1) {
                        cancelCurrentTimer();
                        startRound();
                        notifyPlayers("Game in progress: " + gameInProgress);
                    } else {
                        // don't start the round, just notify
                        notifyPlayers("Game in progress: " + gameInProgress + " (Game will try start in 8 seconds)");
                    }

                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private void cancelCurrentTimer() {
        timer.cancel();
        timer.purge();
    }

    public void bet(Player player, int amount) {
        player.bet(amount);
    }

    public void stand(UUID playerId) {
        if(currentPlayer.getId().equals(playerId))
            currentPlayer.setResponded(true);
    }

    public void hit(UUID playerId){
        if(currentPlayer.getId().equals(playerId)){
            dealCardToPlayer(currentPlayer);

            if(currentPlayer.getScore().getPoints() > 21){
                currentPlayer.setResponded(true);
            }
            else {
                cancelCurrentTimer();
                timer = new Timer(String.valueOf(currentPlayer.getId()));
                timer.scheduleAtFixedRate(setupPlayerTimer(), PLAYER_WAITING_TIME, PLAYER_WAITING_TIME);
            }
        }
    }

    public String showCards(){
        return "Your cards: " + currentPlayer.showCards() + "\nDealer cards: " + dealer.showCards() +"\n What is your decision? (You have 4 seconds)";
    }

    public boolean isInGame(UUID playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId))
                return true;
        }

        return false;
    }

    private void notifyPlayers(String message) throws IOException {
        ClientCommunicationManager.getInstance().broadcastStatusMessage(message);
    }

    private void notifyPlayers(MessageWrapper wrapper) throws IOException {
        ClientCommunicationManager.getInstance().broadcastMessage(wrapper);
    }

    private void notifyConcretePlayer(UUID id, String message) throws IOException {
        if (id != dealer.getId()) {
            ClientCommunicationManager.getInstance().sendMessageToClient(id, message);
        }
    }

    private void notifyConcretePlayer(UUID id, EventType eventType, String message) throws IOException {
        if (id != dealer.getId()) {
            ClientCommunicationManager.getInstance().sendMessageToClient(id, eventType, message);
        }
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }


}
