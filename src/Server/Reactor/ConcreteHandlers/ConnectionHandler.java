package Server.Reactor.ConcreteHandlers;

import Common.EventType;
import Common.Game.Player;
import Common.Game.Game;
import Server.ClientCommunicationManager;
import Server.Reactor.Handle;
import Server.Reactor.Interfaces.IEventHandler;
import Server.Reactor.Reactor;
import Server.ServerView;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ConnectionHandler implements IEventHandler {

    // For displaying purposes
    private ServerView view;

    public ConnectionHandler(ServerView view){
        this.view = view;
    }

    @Override
    public void handleEvent(Handle handle) throws IOException {
        EventType eventType = handle.getMessageWrapper().getEventType();

        switch (eventType) {
            case CONNECT: {
                ServerSocketChannel serverSocket = handle.getServerSocket();
                SocketChannel client = serverSocket.accept();
                client.configureBlocking(false);

                client.register(Reactor.getInstance().getDemultiplexer(), SelectionKey.OP_READ);

                view.addText("Accepted new connection from client: " + client);
                break;
            }
            default: break;
        }
    }


}
