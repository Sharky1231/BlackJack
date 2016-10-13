package Server.Reactor.ConcreteHandlers;

import Common.EventType;
import Common.Game.Game;
import Common.Game.Player;
import Common.Messages.BetMessage;
import Common.Messages.JoinMessage;
import Common.Messages.MessageWrapper;
import Server.ClientCommunicationManager;
import Server.Reactor.Handle;
import Server.Reactor.Interfaces.IEventHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class GameHandler implements IEventHandler {
    @Override
    public void handleEvent(Handle handle) throws IOException {
        EventType eventType = handle.getMessageWrapper().getEventType();
        switch (eventType){
            case JOIN:{
                // Do whatever needs to be done in the server. Write to handle to return results.
                JoinMessage message = (JoinMessage)handle.getMessageWrapper().getMessage();
                System.out.println("JOIN EVENT EXECUTED. New client with id: " + message.getClientId() + " joined.");

                UUID clientId = message.getClientId();
                ClientCommunicationManager.getInstance().addClient(clientId, handle.getClientChannel());
                // Add the new connection to the selector
                Game.getInstance().addPlayer(new Player(clientId, 100));
                try {
                    ClientCommunicationManager.getInstance().sendMessageToClient(clientId, new MessageWrapper( EventType.JOIN, new BetMessage(55)));
                    Thread.sleep(200);
                    ClientCommunicationManager.getInstance().sendMessageToClient(clientId, new MessageWrapper( EventType.BET, new BetMessage(55)));
                    Thread.sleep(200);
                    ClientCommunicationManager.getInstance().sendMessageToClient(clientId, new MessageWrapper( EventType.JOIN, new BetMessage(55)));
                    Thread.sleep(200);
                    ClientCommunicationManager.getInstance().sendMessageToClient(clientId, new MessageWrapper( EventType.JOIN, new BetMessage(55)));
                    Thread.sleep(200);
                    ClientCommunicationManager.getInstance().sendMessageToClient(clientId, new MessageWrapper( EventType.JOIN, new BetMessage(55)));
                    Thread.sleep(200);
                    ClientCommunicationManager.getInstance().sendMessageToClient(clientId, new MessageWrapper( EventType.BET, new BetMessage(55)));
                    Thread.sleep(200);
                    ClientCommunicationManager.getInstance().sendMessageToClient(clientId, new MessageWrapper( EventType.BET, new BetMessage(55)));
                    Thread.sleep(200);
                    ClientCommunicationManager.getInstance().sendMessageToClient(clientId, new MessageWrapper( EventType.BET, new BetMessage(55)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            }
            case BET:{
                // Do whatever needs to be done in the server. Write to handle to return results.
                System.out.println("BET EVENT EXECUTED");
                break;
            }
            case HIT:{
                System.out.println("shit");
                break;
            }
            case STAND:{
                System.out.println("oh no");
                break;
            }
            default: break;
        }
    }
}
