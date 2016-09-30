package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ClientController {
    private ClientView view;

    public ClientController(ClientView view){
        this.view = view;
        view.registerEvents(this);
    }


    public void handleButtonEvent(ActionEvent e){
        if (((JButton) e.getSource()).getText().startsWith("Join game")) {
            view.addText("Connecting to the game...");
        }
        else if (((JButton) e.getSource()).getText().startsWith("Leave")) {
            view.addText("Disconnected.");
        }
        else if (((JButton) e.getSource()).getText().startsWith("Bet")) {
            view.addText("Bet");
        }
        else if (((JButton) e.getSource()).getText().startsWith("Another card")) {
            view.addText("Another card");
        }
        else if (((JButton) e.getSource()).getText().startsWith("Stay")) {
            view.addText("Stay");
        }
    }
}
