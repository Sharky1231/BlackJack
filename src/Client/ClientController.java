package Client;

import Client.Messages.BetMessage;
import Client.Messages.MessageWrapper;
import Common.EventType;
import Common.Serializer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientController {
    private ClientView view;

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

            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 6666);
            SocketChannel client = SocketChannel.open(hostAddress);

            System.out.println("Client sending messages to server...");

            BetMessage msg = new BetMessage(50);
            MessageWrapper wrapper = new MessageWrapper(EventType.BET, msg);

            byte [] message = Serializer.serialize(wrapper);
            ByteBuffer buffer = ByteBuffer.wrap(message);
            System.out.println(message);
            client.write(buffer);
            buffer.clear();
            Thread.sleep(2000);
            System.out.println("CLIENT DIED.");
            client.close();

            Thread.sleep(3000);
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
            client.close();
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
        else if (((JButton) e.getSource()).getText().startsWith("Leave")) {
            view.addText("Disconnected.");
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
