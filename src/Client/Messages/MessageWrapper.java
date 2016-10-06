package Client.Messages;

import Common.EventType;

import java.io.Serializable;

public class MessageWrapper implements Serializable{
    private EventType eventType;
    private Object message;

    public MessageWrapper(EventType eventType, Object message) {
        this.eventType = eventType;
        this.message = message;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Object getMessage() {
        return message;
    }
}
