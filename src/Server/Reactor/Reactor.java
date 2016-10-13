package Server.Reactor;


import Common.Messages.MessageWrapper;
import Common.EventType;
import Common.Serializer;
import Server.Reactor.Interfaces.IEventHandler;
import Server.Reactor.Interfaces.IReactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class Reactor implements IReactor {

    private static Reactor reactor;
    private Map<EventType, IEventHandler> registeredHandlers;
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
        //Handle handle = handler.getHandle();
        registeredHandlers.put(eventType, handler);
    }

    @Override
    public void remove_handler(EventType eventType, IEventHandler handler) {
        registeredHandlers.remove(eventType, handler);
    }

    @Override
    public void handle_events() throws IOException {
        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 6666);
            serverSocket.bind(hostAddress);
            serverSocket.configureBlocking(false);
            int ops = serverSocket.validOps();
            SelectionKey selectKy = serverSocket.register(demultiplexer, ops, null);

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
                        Handle handle = new Handle(new MessageWrapper(EventType.CONNECT, null), serverSocket, null);
                        handler.handleEvent(handle);

                    } else if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1500);
                        client.read(buffer);
                        MessageWrapper wrapper = (MessageWrapper) Serializer.deserialize(buffer.array());

                        // Read the data from client
                        IEventHandler handler = registeredHandlers.get(wrapper.getEventType());
                        Handle handle = new Handle(wrapper, null, client);
                        handler.handleEvent(handle);

                        //client.write(buffer);

                        client.close();
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
