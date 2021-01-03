package ro.alexpopa;

import mpi.MPI;
import ro.alexpopa.msg.CloseMessage;
import ro.alexpopa.msg.Message;
import ro.alexpopa.msg.SubscribeMessage;
import ro.alexpopa.msg.UpdateMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DSM {

    public Map<String, Set<Integer>> subscribers;
    public int a, b, c;
    public static final Lock lock = new ReentrantLock();

    public DSM() {
        a = 0;
        b = 1;
        c = 2;
        subscribers = new ConcurrentHashMap<>();
        subscribers.put("a", new HashSet<>());
        subscribers.put("b", new HashSet<>());
        subscribers.put("c", new HashSet<>());
    }

    public void updateVariable(String var, int value) {
        lock.lock();
        this.setVariable(var, value);
        Message message = new UpdateMessage(var, value);
        this.sendToSubscribers(var, message);
        lock.unlock();
    }

    public void setVariable(String var, int value) {
        if (var.equals("a"))
            this.a = value;
        if (var.equals("b"))
            this.b = value;
        if (var.equals("c"))
            this.c = value;
    }

    public void checkAndReplace(String var, int old, int newValue) {
        if (var.equals("a") && this.a == old){
            updateVariable("a", newValue);
        }
        if (var.equals("b")&& this.b == old){
            updateVariable("b", newValue);
        }
        if (var.equals("c") && this.c == old){
            updateVariable("c", newValue);
        }
    }

    public void subscribeTo(String var) {
        Set<Integer> subs = this.subscribers.get(var);
        subs.add(MPI.COMM_WORLD.Rank());
        this.subscribers.put(var, subs);
        this.sendAll(new SubscribeMessage(var, MPI.COMM_WORLD.Rank()));
    }

    public void syncSubscription(String var, int rank) {
        Set<Integer> subs = this.subscribers.get(var);
        subs.add(rank);
        this.subscribers.put(var, subs);
    }

    public void sendToSubscribers(String var, Message message) {
        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            if (MPI.COMM_WORLD.Rank() == i || !subscribers.get(var).contains(i))
                continue;

            MPI.COMM_WORLD.Send(new Object[]{message}, 0, 1, MPI.OBJECT, i, 0);
        }
    }

    private void sendAll(Message message) {
        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            if (MPI.COMM_WORLD.Rank() == i && !(message instanceof CloseMessage))
                continue;
            MPI.COMM_WORLD.Send(new Object[]{message},0,1,MPI.OBJECT, i, 0);
        }
    }

    public void close() {
        this.sendAll(new CloseMessage());
    }

}
