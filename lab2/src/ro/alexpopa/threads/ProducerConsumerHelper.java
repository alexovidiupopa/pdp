package ro.alexpopa.threads;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerHelper {
    public static final int CAPACITY = 1;
    public final Queue queue = new LinkedList();

    public Lock lock = new ReentrantLock();
    public Condition condVar = lock.newCondition();

    public void put(int val) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == CAPACITY) {
                System.out.println(Thread.currentThread().getName()
                        + ": Buffer is full, waiting");
                condVar.await();
            }

            boolean isAdded = queue.offer(val);
            if (isAdded) {
                System.out.printf("%s added %d into queue %n", Thread
                        .currentThread().getName(), val);

                // signal consumer thread that, buffer has element now
                System.out.println(Thread.currentThread().getName()
                        + ": Signalling that buffer is not empty anymore");
                condVar.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public int get() throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == 0) {
                System.out.println(Thread.currentThread().getName()
                        + ": Buffer is empty, waiting");
                condVar.await();
            }

            Integer value = (Integer) queue.poll();
            if (value != null) {
                System.out.printf("%s consumed %d from queue %n", Thread
                        .currentThread().getName(), value);

                // signal producer thread that, buffer may be empty now
                System.out.println(Thread.currentThread().getName()
                        + ": Signalling that buffer may be empty now");
                condVar.signal();
            }
            return value;
        } finally {
            lock.unlock();
        }
    }
}
