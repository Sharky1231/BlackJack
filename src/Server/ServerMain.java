package Server;

import Common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerMain {

    public static void main(String[] args) {

        ServerView view = new ServerView();
        ServerController controller = new ServerController(view);
        view.open();

        try{
            ServerSocket serverSocket = new ServerSocket(6666);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;

            // Initiate conversation with client
            GameProtocol kkp = new GameProtocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;

            }

        }
        catch (Exception e){
            System.out.println("Error occurred: " + e.getMessage());

        }
    }
}
