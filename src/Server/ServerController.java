package Server;


import Common.Game.Game;
import Server.Reactor.ConcreteHandlers.ConnectionHandler;
import Server.Reactor.ConcreteHandlers.GameHandler;
import Common.EventType;
import Server.Reactor.Reactor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class ServerController {

    private ServerView view;

    public ServerController(ServerView view){
        this.view = view;
        view.registerEvents(this);
    }

    public void startServer() throws IOException {

        //create the server socket
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 6666);
        serverSocket.bind(hostAddress);
        serverSocket.configureBlocking(false);

        try{
            Thread serverThread = new Thread(() -> {
                try {
                    Reactor reactor = Reactor.getInstance();
                    reactor.setServerSocket(serverSocket);
                    Game game = Game.getInstance();
                    game.startGame();

                    // The view is only passed for the debugging/displaying purposes
                    ConnectionHandler connectionHandler = new ConnectionHandler(view);
                    GameHandler gameHandler = new GameHandler(view);

                    reactor.register_handler(EventType.CONNECT, connectionHandler);
                    reactor.register_handler(EventType.JOIN, gameHandler);
                    reactor.register_handler(EventType.BET, gameHandler);
                    reactor.register_handler(EventType.STAND, gameHandler);
                    reactor.register_handler(EventType.HIT, gameHandler);

                    reactor.handle_events();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            serverThread.start();
        }
        catch (Exception e){
            view.addText("Error occurred: " + e.getMessage());
        }
    }

    public void handleButtonEvent(ActionEvent e) {
        if (((JButton) e.getSource()).getText().startsWith("START")) {
            view.addText("Server is warming up...");
            try {
                startServer();
                view.addText("Server started!");
            }
            catch ( Exception ex )
            {
                view.addText("There was an error while staring the server. " + ex.getMessage());
            }
        }
        else if (((JButton) e.getSource()).getText().startsWith("EXIT")) {
            view.addText("Server shuts down.");
            System.exit(0);
        }
    }
}
