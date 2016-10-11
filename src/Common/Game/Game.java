package Common.Game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private ArrayList<Player> players;
    private CardPack cardPack;
    private Player dealer;
    private Timer timer;
    private TimerTask timerTask;
    private boolean gameInProgress;

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
        this.dealer = new Player(1, 1500);
        addPlayer(dealer);
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public boolean playerWon(Player player){
        int playerScore = player.getScore().getPoints();
        int dealerScore = dealer.getScore().getPoints();

        return playerScore > dealerScore;
    }

    public void updateMoneyAmounts(){
        for(Player player : players){
            if(playerWon(player)){
                player.increaseMoney(player.getBet() * 2);
                dealer.increaseMoney(-player.getBet());
            }
            else {
                dealer.increaseMoney(player.getBet());
            }
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
        this.timer = new Timer("GameTimer");
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                if(players.size() > 1){
                    gameInProgress = true;
                    timer.cancel();
                    try {
                        startPlayerTimer();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("gameinprogress: " +gameInProgress);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 8*1000);
    }

    private void startPlayerTimer() throws InterruptedException {
        this.timer = new Timer("PlayerTimer");
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                dealer.setResponded(true);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 4*1000);

        while (!dealer.getResponded()){
            Thread.sleep(500);
        }

        System.out.println("dealer responded: " +dealer.getResponded());
    }


}
