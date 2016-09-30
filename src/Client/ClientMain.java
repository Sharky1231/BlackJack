package Client;

public class ClientMain {

    public static void main(String[] args) {

        ClientView view = new ClientView();
        ClientController controller = new ClientController(view);
        view.open();
    }

}
