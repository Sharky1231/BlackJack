package Client;

import Common.Messages.ClientToServer.BetMessage;
import Common.Messages.ClientToServer.JoinMessage;
import Common.Messages.MessageWrapper;
import Common.EventType;
import Common.Messages.ServerToClient.StatusMessage;
import Common.Serializer;
import sun.security.krb5.internal.HostAddress;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class ClientController {
    private ClientView view;
    // include here client id and send it with every request?

    private static UUID clientId = UUID.randomUUID();
    private SocketChannel client;
    private InetSocketAddress hostAddress;
    private int portNumber = 6666;

    public ClientController(ClientView view) {
        this.view = view;
        view.registerEvents(this);
    }

    private void connect() throws IOException, InterruptedException {
        hostAddress = new InetSocketAddress("localhost", portNumber);
        client = SocketChannel.open(hostAddress);
        client.configureBlocking(false);

        System.out.println("Client sending messages to server...Client id: " + clientId);

        JoinMessage msg = new JoinMessage(clientId);
        MessageWrapper wrapper = new MessageWrapper(clientId, EventType.JOIN, msg);
        sendMessage(wrapper);

        // Selector: multiplexer of SelectableChannel objects
        Selector selector = Selector.open(); // selector is open here
        int ops = client.validOps();
        SelectionKey selectKy = client.register(selector, ops, null);


        Thread clientMessageReadingThread = new Thread(() -> {
            try {
                // Infinite loop..
                // Keep client running
                while (true) {

                    // Selects a set of keys whose corresponding channels are ready for I/O operations
                    selector.select();

                    // token representing the registration of a SelectableChannel with a Selector
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey myKey = iterator.next();

                        if (myKey.isReadable()) {

                            SocketChannel clientChannel = (SocketChannel) myKey.channel();
                            ByteBuffer serverBuffer = ByteBuffer.allocate(1000);
                            clientChannel.read(serverBuffer);

                            byte[] receivedByteArray = serverBuffer.array();

                            if (receivedByteArray.length > 0) {
                                MessageWrapper messageWrapper = (MessageWrapper) Serializer.deserialize(receivedByteArray);
                                EventType eventType = messageWrapper.getEventType();

                                handleMessageEvent(eventType, messageWrapper);
                            }

                        }
                        iterator.remove();
                    }
                }
            } catch (Exception e) {
                view.addText("Exception occured in selector thread. " + e.getMessage());
            }
        });
        clientMessageReadingThread.start();
    }

    private void handleMessageEvent(EventType eventType, MessageWrapper wrapper) {
        StatusMessage statusMessage = (StatusMessage) wrapper.getMessage();
        switch (eventType) {
            case STATUS: {
                view.addText(statusMessage.getUpdateString());
                break;
            }
            case SUCCESS: {
                view.clientBettedButtons();
                view.addText(statusMessage.getUpdateString());
                break;
            }

            case NOT_SUCCESS: {
                view.addText(statusMessage.getUpdateString());
                break;
            }

            case GAME_FINISHED: {
                view.gameRestartButtons();
                view.addText(statusMessage.getUpdateString());
                break;
            }
            case CARD_REQUESTED: {
                view.addText(statusMessage.getUpdateString());
                break;
            }

        }
    }

    public void sendMessage(MessageWrapper wrapper) throws IOException {
        byte[] message = Serializer.serialize(wrapper);
        ByteBuffer buffer = ByteBuffer.wrap(message);
        client.write(buffer);
        buffer.clear();
    }


    public void handleButtonEvent(ActionEvent actionEvent) throws IOException, InterruptedException {
        try {
            handleAction(actionEvent);
        } catch (Exception ex) {
            view.addText("Exception occured while connecting. " + ex.getMessage());
        }
    }

    private void handleAction(ActionEvent e) throws IOException, InterruptedException {
        if (((JButton) e.getSource()).getText().startsWith("Join game")) {
            view.addText("Connecting to the game...");
            connect();
            view.clientJoined();
        } else if (((JButton) e.getSource()).getText().startsWith("Exit")) {
            view.addText("Disconnected.");
            System.exit(0);
        } else if (((JButton) e.getSource()).getText().startsWith("Bet")) {
            sendMessage(new MessageWrapper(clientId, EventType.BET, new BetMessage(view.getBet())));
        } else if (((JButton) e.getSource()).getText().startsWith("Another card")) {
            sendMessage(new MessageWrapper(clientId, EventType.HIT, null));
            view.addText("Another card");

        } else if (((JButton) e.getSource()).getText().startsWith("Stay")) {
            sendMessage(new MessageWrapper(clientId, EventType.STAND, null));
            view.addText("Stay");
            view.clientStayed();
        }
    }
}
