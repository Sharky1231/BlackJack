package Server;

public class ServerMain {

    public static void main(String[] args) {

        ServerView view = new ServerView();
        ServerController controller = new ServerController(view);
        view.open();
    }
}
