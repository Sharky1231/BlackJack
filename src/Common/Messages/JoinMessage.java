package Common.Messages;

/**
 * Created by stefa on 10/12/2016.
 */
import java.io.Serializable;
import java.util.UUID;

public class JoinMessage implements Serializable{
    UUID clientId;

    public JoinMessage(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getClientId() {
        return clientId;
    }
}

