package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {
    private ClientView view;

    public ClientController(ClientView view){
        this.view = view;
        view.registerEvents(this);
    }

    private void connect() {
        int portNumber = 6666;

        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");

            Socket kkSocket = new Socket(serverAddress, portNumber);
            Scanner userInput = new Scanner(System.in);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(kkSocket.getInputStream()));

            view.addText("Connected!");
            out.println("Client connected.");


        }
        catch (Exception e){
            view.addText("Error occurred: " + e.getMessage());
        }
    }


    public void handleButtonEvent(ActionEvent e){
        if (((JButton) e.getSource()).getText().startsWith("Join game")) {
            view.addText("Connecting to the game...");
            connect();
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
