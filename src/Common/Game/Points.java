package Common.Game;

public class Points {
    private int lowPoints;
    private int highPoints;

    public Points(){
        lowPoints = 0;
        highPoints = 0;
    }

    public void update(Card card){
        int points = card.getValue();

        if(points == 1) {
            lowPoints += points;
            highPoints += 11;
        }
        else {
            lowPoints += points;
            highPoints += points;
        }
    }

    public int getLowPoints(){
        return lowPoints;
    }

    public int getHighPoints(){
        return highPoints;
    }

    public int getPoints(){
        if(highPoints < 21)
            return highPoints;
        return lowPoints;
    }

    public String showPoints(){
        if (lowPoints == highPoints)
            return String.valueOf(lowPoints);

        return String.valueOf(lowPoints) + "/" + String.valueOf(highPoints);
    }
}
