package ro.alexpopa.runner;

import ro.alexpopa.model.Matrix;
import ro.alexpopa.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRunner {

    public static void run(Matrix a, Matrix b, Matrix c, int noThreads, String threadType){
        ExecutorService service = Executors.newFixedThreadPool(noThreads);

        switch (threadType) {
            case "Row":
                for (int i=0;i<noThreads;i++)
                    service.submit(Utils.createRowThread(i, a, b, c, noThreads));
                break;
            case "Column":
                for (int i=0;i<noThreads;i++)
                    service.submit(Utils.createColumnThread(i, a, b, c, noThreads));
                break;
            case "Kth":
                for (int i=0;i<noThreads;i++)
                    service.submit(Utils.createKthThread(i, a, b, c, noThreads));
                break;
            default:
                System.err.println("Invalid strategy");
                break;
        }

        service.shutdown();
        try {
            if (!service.awaitTermination(300, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
            System.out.println("result:");
            System.out.println(c.toString());
        } catch (InterruptedException ex) {
            service.shutdownNow();
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
