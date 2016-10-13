package Common.Messages;

import Common.EventType;

import java.io.Serializable;
import java.util.UUID;

public class MessageWrapper implements Serializable{
    private UUID senderId;
    private EventType eventType;
    private Object message;

    public MessageWrapper(EventType eventType, Object message) {
        this.eventType = eventType;
        this.message = message;
        this.senderId = null;
    }

    public MessageWrapper(UUID senderId, EventType eventType, Object message) {
        this.eventType = eventType;
        this.message = message;
        this.senderId = senderId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Object getMessage() {
        return message;
    }

    public UUID getSenderId(){ return senderId;}
}
