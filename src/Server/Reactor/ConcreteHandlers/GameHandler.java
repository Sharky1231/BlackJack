package Server.Reactor.ConcreteHandlers;

import Common.EventType;
import Common.Game.Game;
import Server.Reactor.Handle;
import Server.Reactor.Interfaces.IEventHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class GameHandler implements IEventHandler {
    @Override
    public void handleEvent(Handle handle) throws IOException {
        EventType eventType = handle.getMessageWrapper().getEventType();
        switch (eventType){
            case BET:{
                // Do whatever needs to be done in the server. Write to handle to return results.
//                System.out.println("BET EVENT EXECUTED");
                break;
            }
            case HIT:{
                System.out.println("shit");

                break;
            }
            case STAND:{
                System.out.println("oh no");
                break;
            }
            default: break;
        }
    }
}
