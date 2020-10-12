package ro.alexpopa.threads;

public class ConsumerThread extends Thread {
    public int result = 0;
    public ProducerConsumerHelper helper;
    public int length;

    public ConsumerThread(ProducerConsumerHelper helper, int length) {
        super("Consumer");
        this.helper = helper;
        this.length = length;
    }

    @Override
    public void run() {
        for (int i=0;i<this.length;i++) {
            try {
                result+=helper.get();
                System.out.printf("Consumer: Result is now %d\n", result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("\nConsumer: Final result is: %d", result);
    }
}
