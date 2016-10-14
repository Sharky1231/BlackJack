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
import Server.ServerView;

import java.io.IOException;
import java.util.UUID;

public class GameHandler implements IEventHandler {

    private ServerView view;

    public GameHandler(ServerView view) {
        this.view = view;
    }

    @Override
    public void handleEvent(Handle handle) throws IOException, InterruptedException {
        EventType eventType = handle.getMessageWrapper().getEventType();
        switch (eventType) {
            case JOIN: {
                JoinMessage message = (JoinMessage) handle.getMessageWrapper().getMessage();

                UUID clientId = message.getClientId();
                ClientCommunicationManager.getInstance().addClient(clientId, handle.getClientChannel());
                String statusMessage = "New client joined the game. Client ID: " + clientId;

                view.addText(statusMessage);
                ClientCommunicationManager.getInstance().broadcastStatusMessage(statusMessage);
                ;
                break;
            }
            case BET: {
                MessageWrapper wrapper = handle.getMessageWrapper();
                BetMessage betMessage = (BetMessage) wrapper.getMessage();
                String messageToClient = "";

                if (Game.getInstance().isGameInProgress()) {
                    messageToClient = "Sorry, game is in progress.";
                    ClientCommunicationManager.getInstance().sendMessageToClient(wrapper.getSenderId(), new MessageWrapper(EventType.NOT_SUCCESS, new StatusMessage(messageToClient)));
                    view.addText("Client " + wrapper.getSenderId() + " failed to BET. Reason: " + messageToClient);
                } else {
                    messageToClient = "You have have successfully betted: " + betMessage.getAmount();
                    Player player = new Player(wrapper.getSenderId());

                    if (!Game.getInstance().isInGame(player.getId())) {
                        Game.getInstance().addPlayer(player);
                    }
                    Game.getInstance().bet(player, betMessage.getAmount());
                    ClientCommunicationManager.getInstance().sendMessageToClient(wrapper.getSenderId(), new MessageWrapper(EventType.SUCCESS, new StatusMessage(messageToClient)));
                    view.addText("Client " + wrapper.getSenderId() + " successfully bet  " + betMessage.getAmount());
                }
                break;
            }
            case HIT: {
                MessageWrapper wrapper = handle.getMessageWrapper();
                Game.getInstance().hit(wrapper.getSenderId());
                view.addText("Client " + wrapper.getSenderId() + " HIT.");
                ClientCommunicationManager.getInstance().sendMessageToClient(wrapper.getSenderId(), new MessageWrapper(EventType.CARD_REQUESTED, new StatusMessage(Game.getInstance().showCards())));
                break;
            }
            case STAND: {
                MessageWrapper wrapper = handle.getMessageWrapper();
                Game.getInstance().stand(wrapper.getSenderId());
                view.addText("Client " + wrapper.getSenderId() + " STAND.");
                break;
            }
            default:
                break;
        }
    }
}
