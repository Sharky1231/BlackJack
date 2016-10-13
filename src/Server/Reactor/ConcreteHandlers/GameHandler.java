package Server.Reactor.ConcreteHandlers;

import Common.EventType;
import Common.Game.Game;
import Common.Game.Player;
import Common.Messages.ClientToServer.BetMessage;
import Common.Messages.ClientToServer.JoinMessage;
import Common.Messages.MessageWrapper;
import Common.Messages.ServerToClient.StatusMessage;
import Server.ClientCommunicationManager;
import Server.Reactor.Handle;
import Server.Reactor.Interfaces.IEventHandler;

import java.io.IOException;
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
                    String statusMessage = "New client joined the table. Client ID: " + clientId;
                    //ClientCommunicationManager.getInstance().sendMessageToClient(clientId, new MessageWrapper( EventType.JOIN, new BetMessage(55)));
                    ClientCommunicationManager.getInstance().broadcastMessage(new MessageWrapper(EventType.STATUS, new StatusMessage(statusMessage)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            }
            case BET:{
                // Do whatever needs to be done in the server. Write to handle to return results.
//                System.out.println("BET EVENT EXECUTED");
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
