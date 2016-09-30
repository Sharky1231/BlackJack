package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {

        ClientView view = new ClientView();
        ClientController controller = new ClientController(view);
        view.open();

        int portNumber = 6666;

        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");

            Socket kkSocket = new Socket(serverAddress, portNumber);
            Scanner userInput = new Scanner(System.in);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(kkSocket.getInputStream()));

            String fromServer, fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;

                fromUser = userInput.nextLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        }
        catch (Exception e){
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

}
