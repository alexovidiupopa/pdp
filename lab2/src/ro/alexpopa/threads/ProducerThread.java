package ro.alexpopa.threads;

import ro.alexpopa.domain.Vector;

public class ProducerThread extends Thread {

    public int length;
    public ProducerConsumerHelper helper;
    public Vector vec1,vec2;

    public ProducerThread(ProducerConsumerHelper helper, Vector vec1, Vector vec2) {
        super("Producer");
        this.helper = helper;
        this.vec1 = vec1;
        this.vec2 = vec2;
        this.length = vec1.getLength();
    }

    public ProducerThread(ProducerConsumerHelper helper) {
        super("Producer");
        this.helper = helper;
    }

    @Override
    public void run() {
        for (int i=0;i<length;i++){
            try {
                System.out.printf("Producer: Sending %d * %d = %d\n",vec1.get(i),vec2.get(i),vec1.get(i)*vec2.get(i));
                helper.put(vec1.get(i)*vec2.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
