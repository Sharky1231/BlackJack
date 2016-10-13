package Common.Messages.ServerToClient;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by stefa on 10/13/2016.
 */
public class StatusMessage implements Serializable {
    private String updateString;

    public StatusMessage(String updateString) {
        this.updateString = updateString;
    }

    public String getUpdateString() {
        return this.updateString;
    }
}
