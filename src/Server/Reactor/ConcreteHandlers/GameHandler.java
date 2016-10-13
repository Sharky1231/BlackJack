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
                JoinMessage message = (JoinMessage)handle.getMessageWrapper().getMessage();
                System.out.println("JOIN EVENT EXECUTED. New client with id: " + message.getClientId() + " joined.");

                UUID clientId = message.getClientId();
                ClientCommunicationManager.getInstance().addClient(clientId, handle.getClientChannel());

                // Add player in BET event?
//                Game.getInstance().addPlayer(new Player(clientId));
                try {
                    String statusMessage = "New client joined the game. Client ID: " + clientId;
                    ClientCommunicationManager.getInstance().broadcastStatusMessage(statusMessage);;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            }
            case BET:{
                MessageWrapper wrapper = (MessageWrapper) handle.getMessageWrapper();
                BetMessage betMessage = (BetMessage) wrapper.getMessage();
                String messageToClient = "";

                if(Game.getInstance().isGameInProgress()){
                    messageToClient = "Game is in progress";
                    ClientCommunicationManager.getInstance().sendMessageToClient(wrapper.getSenderId(), new MessageWrapper(EventType.NOT_SUCCESS, new StatusMessage(messageToClient)));
                }
                else {
                    messageToClient = "You have have successfully bet: " + betMessage.getAmount();
                    Player player = new Player(wrapper.getSenderId());
                    Game.getInstance().addPlayer(player);
                    Game.getInstance().bet(player, betMessage.getAmount());
                    ClientCommunicationManager.getInstance().sendMessageToClient(wrapper.getSenderId(), new MessageWrapper(EventType.SUCCESS, new StatusMessage(messageToClient)));
                }
                break;
            }

            // SEND UUID WITH EACH MESSAGE
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
