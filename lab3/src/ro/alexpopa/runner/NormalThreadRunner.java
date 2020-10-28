package ro.alexpopa.runner;

import ro.alexpopa.model.Matrix;
import ro.alexpopa.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NormalThreadRunner {
    public static void run(Matrix a, Matrix b, Matrix c, int noThreads, String threadType){
        List<Thread> threadsList = new ArrayList<>();

        switch (threadType) {
            case "Row":
                for (int i=0;i<noThreads;i++)
                    threadsList.add(Utils.createRowThread(i, a, b, c, noThreads));
                break;
            case "Column":
                for (int i=0;i<noThreads;i++)
                    threadsList.add(Utils.createColumnThread(i, a, b, c, noThreads));

                break;
            case "Kth":
                for (int i=0;i<noThreads;i++)
                    threadsList.add(Utils.createKthThread(i, a, b, c, noThreads));
                break;
            default:
                System.err.println("Invalid strategy");
                return;
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
        System.out.println("result:");
        System.out.println(c);
    }
}
