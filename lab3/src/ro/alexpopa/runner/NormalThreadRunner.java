package ro.alexpopa.runner;

import ro.alexpopa.model.Matrix;
import ro.alexpopa.model.MatrixException;
import ro.alexpopa.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public final class NormalThreadRunner {
    public static void run(Matrix a, Matrix b, Matrix c, int noThreads, String threadType) throws MatrixException {
        List<Thread> threadsList = new ArrayList<>();

        switch (threadType) {
            case "Row":
                for (int i=0;i<noThreads;i++)
                    threadsList.add(Utils.initRowThread(i, a, b, c, noThreads));
                break;
            case "Column":
                for (int i=0;i<noThreads;i++)
                    threadsList.add(Utils.initColThread(i, a, b, c, noThreads));

                break;
            case "Kth":
                for (int i=0;i<noThreads;i++)
                    threadsList.add(Utils.initKThread(i, a, b, c, noThreads));
                break;
            default:
                throw new MatrixException("Invalid strategy");
        }

        for (Thread thread : threadsList) {
            thread.start();
        }
        for (Thread thread : threadsList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("result:\n" + c.toString());
    }
}
