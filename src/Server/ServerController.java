package Server;


import javax.swing.*;
import java.awt.event.ActionEvent;

public class ServerController {

    private ServerView view;

    public ServerController(ServerView view){
        this.view = view;
        view.registerEvents(this);
    }
    public void handleButtonEvent(ActionEvent e){
        if (((JButton) e.getSource()).getText().startsWith("START")) {
            view.addText("Server is warming up...");
        }
        else if (((JButton) e.getSource()).getText().startsWith("STOP")) {
            view.addText("Server shut down.");
        }
    }
}
