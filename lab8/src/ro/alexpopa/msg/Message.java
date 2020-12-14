package ro.alexpopa.msg;

import java.io.Serializable;

public class Message implements Serializable {

    public UpdateMessage updateMessage = null;
    public ChangeMessage changeMessage = null;
    public SubscribeMessage subscribeMessage = null;

    public boolean done = false;

    public Message(boolean done) {
        this.done = done;
    }

    public Message(SubscribeMessage subscribeMessage) {
        this.subscribeMessage = subscribeMessage;
    }

    public Message(ChangeMessage changeMessage) {
        this.changeMessage = changeMessage;
    }

    public Message(UpdateMessage updateMessage) {
        this.updateMessage = updateMessage;
    }
}
