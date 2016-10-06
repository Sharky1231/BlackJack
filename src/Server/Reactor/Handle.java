package Server.Reactor;

import Client.Messages.MessageWrapper;
import Common.EventType;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Handle {

    MessageWrapper wrapper;
    ServerSocketChannel serverSocket;
    SocketChannel clientChannel;


    public Handle(MessageWrapper wrapper, ServerSocketChannel serverSocket, SocketChannel clientChannel){

        this.wrapper = wrapper;
        this.serverSocket = serverSocket;
        this.clientChannel = clientChannel;
    }


    public MessageWrapper getMessageWrapper(){
        return wrapper;
    }

    public SocketChannel getClientChannel() {
        return clientChannel;
    }

    public ServerSocketChannel getServerSocket() {
        return serverSocket;
    }
}
