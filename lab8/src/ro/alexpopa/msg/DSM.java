package ro.alexpopa.msg;

import mpi.MPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DSM {

    public Map<String, List<Integer>> subscribers;
    public int a, b, c;

    public DSM() {
        a = 0;
        b = 1;
        c = 2;
        subscribers = new HashMap<>();
        subscribers.put("a", new ArrayList<>());
        subscribers.put("b", new ArrayList<>());
        subscribers.put("c", new ArrayList<>());
    }

    public void subscribeTo(String var) {
        List<Integer> subs = this.subscribers.get(var);
        subs.add(MPI.COMM_WORLD.Rank());
        this.subscribers.put(var, subs);
        this.sendAll(new Message(new SubscribeMessage(var, MPI.COMM_WORLD.Rank())));
    }

    public void subscribeOther(String var, int rank) {
        List<Integer> subs = this.subscribers.get(var);
        subs.add(rank);
        this.subscribers.put(var, subs);
    }

    public void sendToSubscribers(String var, Message message) {
        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            if (MPI.COMM_WORLD.Rank() == i || !isSubscribedTo(var, i))
                continue;

            MPI.COMM_WORLD.Send(new Object[]{message}, 0, 1, MPI.OBJECT, i, 0);
        }
    }

    public boolean isSubscribedTo(String var, int rank){
        return subscribers.get(var).contains(rank);
    }

    private void sendAll(Message message) {
        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            if (MPI.COMM_WORLD.Rank() == i)
                continue;
            MPI.COMM_WORLD.Send(new Object[]{message},0,1,MPI.OBJECT, i, 0);
        }
    }

    public void updateVariable(String var, int value) {
        this.setVariable(var, value);
        Message message = new Message(new UpdateMessage(var, value));
        this.sendToSubscribers(var, message);
    }

    public void checkAndReplace(String var, int old, int newValue) {
        if (var.compareTo("a")==0 && this.a == old){
            updateVariable("a", newValue);
        }
        if (var.compareTo("b")==0 && this.b == old){
            updateVariable("b", newValue);
        }
        if (var.compareTo("c")==0 && this.c == old){
            updateVariable("c", newValue);
        }
    }

    public void setVariable(String var, int value) {
        if (var.compareTo("a")==0)
            this.a = value;
        if (var.compareTo("b")==0)
            this.b = value;
        if (var.compareTo("c")==0)
            this.c = value;
    }

    public void close() {
        this.sendAll(new Message(true));
    }

}
