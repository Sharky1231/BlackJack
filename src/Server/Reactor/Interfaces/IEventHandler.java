package Server.Reactor.Interfaces;

import Server.Reactor.Handle;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface IEventHandler {

    void handleEvent(Handle handle) throws IOException;
}
