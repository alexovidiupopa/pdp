package ro.alexpopa.runner;

import ro.alexpopa.model.Matrix;
import ro.alexpopa.model.MatrixException;
import ro.alexpopa.utils.Utils;

import java.util.concurrent.*;

public final class ThreadPoolRunner {

    public static void run(Matrix a, Matrix b, Matrix c, int noThreads, String threadType) throws MatrixException {
        ExecutorService service = Executors.newFixedThreadPool(noThreads);
        //ExecutorService service = new ThreadPoolExecutor(noThreads, noThreads,0L, TimeUnit.SECONDS,
                //new ArrayBlockingQueue<>(noThreads,true));
        switch (threadType) {
            case "Row":
                for (int i=0;i<noThreads;i++)
                    service.submit(Utils.initRowThread(i, a, b, c, noThreads));
                break;
            case "Column":
                for (int i=0;i<noThreads;i++)
                    service.submit(Utils.initColThread(i, a, b, c, noThreads));
                break;
            case "Kth":
                for (int i=0;i<noThreads;i++)
                    service.submit(Utils.initKThread(i, a, b, c, noThreads));
                break;
            default:
                throw new MatrixException("Invalid strategy");
        }

        service.shutdown();
        try {
            if (!service.awaitTermination(300, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
            System.out.println("result:\n" + c.toString());
        } catch (InterruptedException ex) {
            service.shutdownNow();
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
