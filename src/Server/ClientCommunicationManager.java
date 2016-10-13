package Server;

import Common.EventType;
import Common.Messages.MessageWrapper;
import Common.Messages.ServerToClient.StatusMessage;
import Common.Serializer;
import sun.plugin2.message.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by stefa on 10/12/2016.
 */
public class ClientCommunicationManager {

    private static ClientCommunicationManager manager;
    public static ClientCommunicationManager getInstance() {
        if (manager == null)
            manager = new ClientCommunicationManager();
        return manager;
    }

    private ClientCommunicationManager()
    {
        this._clients = new Hashtable<UUID, SocketChannel>();
    }

    private Hashtable<UUID, SocketChannel> _clients;

    public void addClient (UUID clientId, SocketChannel socketChannel )
    {
        this._clients.put( clientId, socketChannel );
    }

    public void sendMessageToClient (UUID clientId, MessageWrapper message ) throws IOException {
        SocketChannel socketChannel = this._clients.get( clientId );

        sendMessageToChannel(socketChannel, message);
    }

    public void sendMessageToClient(UUID clientId, String message) throws IOException {
        sendMessageToClient (clientId, new MessageWrapper(EventType.STATUS, new StatusMessage(message)));
    }

    public void broadcastMessage ( MessageWrapper message ) throws IOException {
        for ( SocketChannel clientSocketChannel : this._clients.values() )
        {
            sendMessageToChannel(clientSocketChannel, message);
        }
    }

    private void sendMessageToChannel ( SocketChannel socketChannel, MessageWrapper message ) throws IOException {
        byte [] byteMessage = Serializer.serialize(message);
        ByteBuffer buffer = ByteBuffer.wrap(byteMessage);

        socketChannel.write(buffer);
        buffer.clear();
    }

    public void sendMessageToClient(SocketChannel socketChannel, MessageWrapper message) throws IOException {
        sendMessageToChannel(socketChannel, message);
    }

    public void sendMessageToClient(UUID id, EventType eventType, String message) throws IOException {
        sendMessageToClient(id, new MessageWrapper(null, eventType, new StatusMessage(message)));
    }

    public void broadcastStatusMessage(String status) throws IOException {
        broadcastMessage(new MessageWrapper(EventType.STATUS, new StatusMessage(status)));
    }

}
