package Server.Reactor.Interfaces;

import Common.EventType;

import java.io.IOException;

public interface IReactor {

    void register_handler(EventType eventType, IEventHandler handler);

    void remove_handler(EventType eventType, IEventHandler handler);

    void handle_events() throws IOException;
}
