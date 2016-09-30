package Server;


import Common.GameProtocol;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {

    private ServerView view;

    public ServerController(ServerView view){
        this.view = view;
        view.registerEvents(this);
    }

    public void startServer()
    {
        try{
            ServerSocket serverSocket = new ServerSocket(6666);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

//            String inputLine, outputLine;
//
//            // Initiate conversation with client
//            GameProtocol kkp = new GameProtocol();
//            outputLine = kkp.processInput(null);
//            out.println(outputLine);
        }
        catch (Exception e){
            view.addText("Error occurred: " + e.getMessage());
        }
    }

    public void handleButtonEvent(ActionEvent e){
        if (((JButton) e.getSource()).getText().startsWith("START")) {
            view.addText("Server is warming up...");
            startServer();
            view.addText("Server started!");
        }
        else if (((JButton) e.getSource()).getText().startsWith("STOP")) {
            view.addText("Server shut down.");
        }
    }
}
