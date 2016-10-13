package Common.Game;

import Server.ClientCommunicationManager;

import Common.EventType;

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
    private static final int GAME_WAITING_TIME = 6*1000;
    private static final int PLAYER_WAITING_TIME = 5*1000;

    private static Game game;

    public static Game getInstance() {
        if (game == null)
            game = new Game();
        return game;
    }

    private Game() {
        this.players = new ArrayList<>();
        this.cardPack = new CardPack();
        this.gameInProgress = false;
        this.dealer = new Player(UUID.randomUUID(), 1500);
        this.currentPlayer = null;
        addPlayer(dealer);
    }

    private TimerTask setupPlayerTimer(){
        return new TimerTask() {

            @Override
            public void run() {
                currentPlayer.setResponded(true);
            }
        };
    }

    private TimerTask setupGameTimer() {
       return new TimerTask() {

            @Override
            public void run() {
                if(players.size() > 1){
                    gameInProgress = true;
                    timer.cancel();
                    timer.purge();
                    try {
                        startPlayerTimer();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("gameinprogress: " +gameInProgress);
            }
        };
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public boolean playerWon(Player player){
        int playerScore = player.getScore().getPoints();
//        System.out.println("playerscore: high: " + player.getScore().getHighPoints() + " low: "+player.getScore().getLowPoints() + " used: " +playerScore);
        int dealerScore = dealer.getScore().getPoints();
//        System.out.println("dealerscore: high: " + dealer.getScore().getHighPoints() + " low: "+dealer.getScore().getLowPoints() + " used: " +dealerScore);

        return playerScore > dealerScore && playerScore <= 21;
    }

    public void updateMoneyAmounts(){
        for(Player player : players){
            System.out.println("Money BEFORE player: "+ player.getMoney() + " dealer: " + dealer.getMoney());
            if(playerWon(player)){
                player.increaseMoney(player.getBet() * 2);
                dealer.increaseMoney(-player.getBet());
            }
            else {
                dealer.increaseMoney(player.getBet());
            }
            System.out.println("Money AFTER player: "+ player.getMoney() + " dealer: " + dealer.getMoney());
        }
    }


    public void dealCardToPlayer(Player player){
        Card randomCard = cardPack.getRandomCard();
        player.putCardIntoHand(randomCard);
    }

    public void startGame(){
        startGameTimer();
    }

    private void startGameTimer() {
        System.out.println("----Game starts----");
        this.timer = new Timer("GameTimer");
        timer.scheduleAtFixedRate(setupGameTimer(), GAME_WAITING_TIME, GAME_WAITING_TIME);
    }

    private void startPlayerTimer() throws InterruptedException {
        this.timer = new Timer("PlayerTimer");


        for(Player player : players){
            currentPlayer = player;
            System.out.println("Current player: " +        currentPlayer.getId() + " cards: "+ currentPlayer.showCards()+" responded: " + currentPlayer.responded());
            currentPlayer.putCardIntoHand(cardPack.getRandomCard());
            currentPlayer.setResponded(true);
            timer = new Timer(String.valueOf(currentPlayer.getId()));
            timer.scheduleAtFixedRate(setupPlayerTimer(), PLAYER_WAITING_TIME, PLAYER_WAITING_TIME);
            while (!currentPlayer.responded()){
                Thread.sleep(500);
            }
            System.out.println("Current player: " +        currentPlayer.getId() + " cards: "+ currentPlayer.showCards()+" responded: " + currentPlayer.responded());
            currentPlayer.setResponded(false);
            timer.cancel();
            timer.purge();
        }

        gameInProgress = false;

        for(Player player : players){
            System.out.println("Player won: " +playerWon(player)+ " playerid: "+player.getId());
        }
        updateMoneyAmounts();

        for(Player player : players){
            player.resetCards();
            player.resetPonts();
        }

        System.out.println("Cards reset.");
        // decide winners and deal money
        // reset cards
//        System.out.println("number Of players: " +        players.size());
//        System.out.println("player removed: " +        players.remove(1).getId());
//        System.out.println("number Of players: " +        players.size());

        System.out.println("----Game ended----");
        startGame();
    }

    public void bet(Player player, int amount){

    }


}
