package Common.Game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class CardPack {
    private ArrayList<Card> cardPack;

    public CardPack() {
        this.cardPack = new ArrayList<>();
        loadPackWithCards();
    }

    public Card getRandomCard(){
        int randomPosition = ThreadLocalRandom.current().nextInt(0, packSize() + 1);
        Card card = removeCardFromDeck(randomPosition);
        return card;
    }

    public Card removeCardFromDeck(int position){
        Card card = cardPack.remove(position);
        return card;
    }

    public int packSize(){
        return cardPack.size();
    }

    private void loadPackWithCards() {
        cardPack.add(new Card(1, "AC"));
        cardPack.add(new Card(2, "2C"));
        cardPack.add(new Card(3, "3C"));
        cardPack.add(new Card(4, "4C"));
        cardPack.add(new Card(5, "5C"));
        cardPack.add(new Card(6, "6C"));
        cardPack.add(new Card(7, "7C"));
        cardPack.add(new Card(8, "8C"));
        cardPack.add(new Card(9, "9C"));
        cardPack.add(new Card(10, "10C"));
        cardPack.add(new Card(10, "JC"));
        cardPack.add(new Card(10, "QC"));
        cardPack.add(new Card(10, "KC"));

        cardPack.add(new Card(1, "AD"));
        cardPack.add(new Card(2, "2D"));
        cardPack.add(new Card(3, "3D"));
        cardPack.add(new Card(4, "4D"));
        cardPack.add(new Card(5, "5D"));
        cardPack.add(new Card(6, "6D"));
        cardPack.add(new Card(7, "7D"));
        cardPack.add(new Card(8, "8D"));
        cardPack.add(new Card(9, "9D"));
        cardPack.add(new Card(10, "10D"));
        cardPack.add(new Card(10, "JD"));
        cardPack.add(new Card(10, "QD"));
        cardPack.add(new Card(10, "KD"));

        cardPack.add(new Card(1, "AH"));
        cardPack.add(new Card(2, "2H"));
        cardPack.add(new Card(3, "3H"));
        cardPack.add(new Card(4, "4H"));
        cardPack.add(new Card(5, "5H"));
        cardPack.add(new Card(6, "6H"));
        cardPack.add(new Card(7, "7H"));
        cardPack.add(new Card(8, "8H"));
        cardPack.add(new Card(9, "9H"));
        cardPack.add(new Card(10, "10H"));
        cardPack.add(new Card(10, "JH"));
        cardPack.add(new Card(10, "QH"));
        cardPack.add(new Card(10, "KH"));

        cardPack.add(new Card(1, "AS"));
        cardPack.add(new Card(2, "2S"));
        cardPack.add(new Card(3, "3S"));
        cardPack.add(new Card(4, "4S"));
        cardPack.add(new Card(5, "5S"));
        cardPack.add(new Card(6, "6S"));
        cardPack.add(new Card(7, "7S"));
        cardPack.add(new Card(8, "8S"));
        cardPack.add(new Card(9, "9S"));
        cardPack.add(new Card(10, "10S"));
        cardPack.add(new Card(10, "JS"));
        cardPack.add(new Card(10, "QS"));
        cardPack.add(new Card(10, "KS"));
    }
}
