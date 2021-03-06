package Server.Reactor;


import Common.Messages.MessageWrapper;
import Common.EventType;
import Common.Serializer;
import Server.Reactor.Interfaces.IEventHandler;
import Server.Reactor.Interfaces.IReactor;
import com.sun.corba.se.spi.activation.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class Reactor implements IReactor {

    private static Reactor reactor;
    private Map<EventType, IEventHandler> registeredHandlers;
    private ServerSocketChannel serverSocketChannel;
    private Selector demultiplexer;

    private Reactor() throws IOException {
        this.registeredHandlers = new HashMap<>();
        this.demultiplexer = Selector.open();
    }

    public static Reactor getInstance() throws IOException {
        if (reactor == null)
            reactor = new Reactor();
        return reactor;
    }

    public Selector getDemultiplexer(){
        return demultiplexer;
    }


    @Override
    public void register_handler(EventType eventType, IEventHandler handler) {
        registeredHandlers.put(eventType, handler);
    }

    @Override
    public void remove_handler(EventType eventType, IEventHandler handler) {
        registeredHandlers.remove(eventType, handler);
    }

    public void setServerSocket ( ServerSocketChannel serverSocketChannel ) throws ClosedChannelException {
        this.serverSocketChannel = serverSocketChannel;

        int ops = serverSocketChannel.validOps();
        this.serverSocketChannel.register(demultiplexer, ops, null);
    }

    @Override
    public void handle_events() throws IOException {
        try {

            while (true) {
                System.out.println("Waiting for select...");

                demultiplexer.select();

                Set selectedKeys = demultiplexer.selectedKeys();
                Iterator iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = (SelectionKey) iter.next();

                    if (key.isAcceptable()) {
                        IEventHandler handler =
                                registeredHandlers.get(EventType.CONNECT);
                        Handle handle = new Handle(new MessageWrapper(null, EventType.CONNECT, null), serverSocketChannel, null);
                        handler.handleEvent(handle);

                    } else if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(2000);
                        client.read(buffer);
                        MessageWrapper wrapper = (MessageWrapper) Serializer.deserialize(buffer.array());

                        // Read the data from client
                        IEventHandler handler = registeredHandlers.get(wrapper.getEventType());
                        Handle handle = new Handle(wrapper, null, client);
                        handler.handleEvent(handle);

                        System.out.println("Client messages are complete; close.");
                    }
                    iter.remove();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
