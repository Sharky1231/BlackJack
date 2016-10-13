package Client;

import Common.Messages.ClientToServer.JoinMessage;
import Common.Messages.MessageWrapper;
import Common.EventType;
import Common.Messages.ServerToClient.StatusMessage;
import Common.Serializer;

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

    public ClientController(ClientView view){
        this.view = view;
        view.registerEvents(this);
    }

    private void connect() throws IOException, InterruptedException {
        int portNumber = 6666;

        try {
//            InetAddress serverAddress = InetAddress.getByName("localhost");
//
//            Socket kkSocket = new Socket(serverAddress, portNumber);
//            Scanner userInput = new Scanner(System.in);
//            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(kkSocket.getInputStream()));
//
//            view.addText("Connected!");
//            out.println("Client connected.");
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", portNumber);
            SocketChannel client = SocketChannel.open(hostAddress);
            client.configureBlocking(false);

            System.out.println("Client sending messages to server...Client id: " + clientId);

            JoinMessage msg = new JoinMessage(clientId);
            MessageWrapper wrapper = new MessageWrapper(EventType.JOIN, msg);

            byte [] message = Serializer.serialize(wrapper);
            ByteBuffer buffer = ByteBuffer.wrap(message);
            System.out.println(message);
            client.write(buffer);
            buffer.clear();


            // Selector: multiplexor of SelectableChannel objects
            Selector selector = Selector.open(); // selector is open here

            int ops = client.validOps();

            SelectionKey selectKy = client.register(selector, ops, null);


            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
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

                                        if (messageWrapper.getEventType().equals(EventType.STATUS)) {
                                            StatusMessage statusMessage = (StatusMessage) messageWrapper.getMessage();

                                            view.addText(statusMessage.getUpdateString());
                                        }
                                    }

                                }
                                iterator.remove();
                            }
                        }
                    } catch (Exception e) {

                    }
                }

            } );
            t1.start();
//            while ( true ) {
//
//                ByteBuffer serverBuffer = ByteBuffer.allocate(1500);
//                client.read(serverBuffer);
//
//                byte[] receivedByteArray = serverBuffer.array();
//
//                if (receivedByteArray.length > 0) {
//                    //MessageWrapper messageWrapper = (MessageWrapper) Serializer.deserialize(receivedByteArray);
//
//                    System.out.println(new String(receivedByteArray));
//                }
//            }

//            // Send messages to server
//            String [] messages = new String [] {"Time goes fast.", "What now?", "Bye."};
//            for (int i = 0; i < messages.length; i++) {
//                byte [] message = new String(messages [i]).getBytes();
//                ByteBuffer buffer = ByteBuffer.wrap(message);
//                client.write(buffer);
//                System.out.println(messages [i]);
//                buffer.clear();
//                Thread.sleep(3000);
//            }

        }

        catch (Exception e){
            view.addText("Error occurred: " + e.getMessage());
        }
    }


    public void handleButtonEvent(ActionEvent e) throws IOException, InterruptedException {
        if (((JButton) e.getSource()).getText().startsWith("Join game")) {
            view.addText("Connecting to the game...");
            connect();
        }
        else if (((JButton) e.getSource()).getText().startsWith("Exit")) {
            view.addText("Disconnected.");
            System.exit(0);
        }
        else if (((JButton) e.getSource()).getText().startsWith("Bet")) {
            view.addText("Bet");
        }
        else if (((JButton) e.getSource()).getText().startsWith("Another card")) {
            view.addText("Another card");
        }
        else if (((JButton) e.getSource()).getText().startsWith("Stay")) {
            view.addText("Stay");
        }
    }
}
