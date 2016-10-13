package Common.Messages.ClientToServer;

import java.io.Serializable;

public class BetMessage implements Serializable{
    int amount;

    public BetMessage(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
