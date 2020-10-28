package ro.alexpopa.threads;

public final class ConsumerThread extends Thread {
    public int result = 0;
    public ProducerConsumerBuffer buffer;
    public int length;

    public ConsumerThread(ProducerConsumerBuffer buffer, int length) {
        super("Consumer");
        this.buffer = buffer;
        this.length = length;
    }

    @Override
    public void run() {
        for (int i=0;i<this.length;i++) {
            try {
                result+=buffer.get();
                System.out.printf("Consumer: Result is now %d\n", result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("\nConsumer: Final result is: %d", result);
    }
}
