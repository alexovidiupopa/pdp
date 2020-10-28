package ro.alexpopa;

import ro.alexpopa.domain.Vector;
import ro.alexpopa.threads.ConsumerThread;
import ro.alexpopa.threads.ProducerThread;
import ro.alexpopa.threads.ProducerConsumerBuffer;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Vector vec1 = new Vector(Arrays.asList(1,2,3,4));
        Vector vec2 = new Vector(Arrays.asList(4,3,2,1));

        ProducerConsumerBuffer helper = new ProducerConsumerBuffer();

        ProducerThread producer = new ProducerThread(helper,vec1,vec2);
        ConsumerThread consumer = new ConsumerThread(helper,vec1.getLength());

        producer.start();
        consumer.start();
    }
}
