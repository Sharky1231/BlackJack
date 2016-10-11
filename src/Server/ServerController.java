package Server;


import Common.Game.Game;
import Server.Reactor.ConcreteHandlers.ConnectionHandler;
import Server.Reactor.ConcreteHandlers.GameHandler;
import Common.EventType;
import Server.Reactor.Reactor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ServerController {

    private ServerView view;

    public ServerController(ServerView view){
        this.view = view;
        view.registerEvents(this);
    }

    public void startServer()
    {
        try{
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Reactor reactor = Reactor.getInstance();
                        Game game = Game.getInstance();
                        game.startGame();
                        ConnectionHandler connectionHandler = new ConnectionHandler();
                        GameHandler gameHandler = new GameHandler();

                        reactor.register_handler(EventType.CONNECT, connectionHandler);
                        reactor.register_handler(EventType.BET, gameHandler);

                        reactor.handle_events();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            t1.start();



//            Socket clientSocket = serverSocket.accept();
//            PrintWriter out =
//                    new PrintWriter(clientSocket.getOutputStream(), true);
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(clientSocket.getInputStream()));

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

    public void handleButtonEvent(ActionEvent e) {
        if (((JButton) e.getSource()).getText().startsWith("START")) {
            view.addText("Server is warming up...");
            startServer();
            view.addText("Server started!");
        }
        else if (((JButton) e.getSource()).getText().startsWith("STOP")) {
            view.addText("Server shuts down.");
            System.exit(0);
        }
    }
}
