package ro.alexpopa;

import mpi.MPI;
import ro.alexpopa.msg.DSM;

import java.io.IOException;

public class Main {

    public static void writeAll(DSM dsm) {
        StringBuilder sb = new StringBuilder();
        sb.append("Write all\n");
        sb.append("Rank ").append(MPI.COMM_WORLD.Rank()).append("->a = ").append(dsm.a).append(" b = ").append(dsm.b).append(" c = ").append(dsm.c).append("\n");
        sb.append("Subscribers: \n");
        for (String var : dsm.subscribers.keySet()) {
            sb.append(var).append(dsm.subscribers.get(var).toString());
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // write your code here
        MPI.Init(args);
        DSM dsm = new DSM();
        int me = MPI.COMM_WORLD.Rank();
        if (me == 0) {
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("a");
            dsm.subscribeTo("b");
            dsm.subscribeTo("c");
            dsm.close();
            thread.join();

        } else if (me == 1) {
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("a");
            dsm.close();
            thread.join();
        } else if (me == 2) {
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("b");
            dsm.checkAndReplace("b",1,100);
            dsm.close();
            thread.join();
        }
        MPI.Finalize();
    }
}
