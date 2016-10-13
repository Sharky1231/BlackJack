package Common.Game;


import java.util.ArrayList;

public class Player {
    private long id;
    private int money;
    private Points score;
    private int bet;
    private boolean responded;
    private ArrayList<Card> cards;

    public Player(long id, int money) {
        this.id = id;
        this.money = money;
        this.bet = 0;
        this.responded = false;
        this.score = new Points();
        this.cards = new ArrayList<>();
    }

    public void putCardIntoHand(Card card){
        cards.add(card);
        score.update(card);
    }

    public int getBet(){
        return bet;
    }

    public void bet(int amount){
        if(bet < amount) {
            this.bet = amount;
            this.money -= bet;
        }
    }

    public Points getScore(){
        return score;
    }

    public boolean responded(){
        return responded;
    }

    public void setResponded(boolean responded){
        this.responded = responded;
    }

    public long getId(){
        return id;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void increaseMoney(int money) {
        this.money += money;
    }

    public void resetCards(){
        cards = new ArrayList<>();
    }

    public String showCards(){
        String string = "";
        for(Card card : cards){
            string += card.toString() + " | ";
        }

        return string;
    }
}
