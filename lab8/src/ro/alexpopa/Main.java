package ro.alexpopa;

import mpi.MPI;
import ro.alexpopa.msg.DSM;
import ro.alexpopa.msg.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void writeAll(DSM dsm) {
        System.out.println("Write all");
        System.out.println("Rank " + MPI.COMM_WORLD.Rank() + "->a = " + dsm.a + " b = " + dsm.b + " c = " + dsm.c + "\n");
        System.out.println("Subscribers: ");
        for (String var : dsm.subscribers.keySet()) {
            System.out.println(var + dsm.subscribers.get(var).toString());
        }
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

            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

            boolean done = false;
            while (!done) {
                System.out.println("1. Set var");
                System.out.println("2. Change var");
                System.out.println("0. Exit");
                System.out.println(">>");
                int ans = Integer.parseInt(obj.readLine());
                if (ans == 0) {
                    dsm.close();
                    done = true;
                } else if (ans == 1) {
                    System.out.println("variable a/b/c: ");
                    String var = obj.readLine();

                    System.out.println("new value (int): ");
                    int value = Integer.parseInt(obj.readLine());

                    dsm.updateVariable(var, value);
                    writeAll(dsm);
                } else if (ans == 2) {
                    System.out.println("variable a/b/c: ");
                    String var = obj.readLine();

                    System.out.println("old value (int): ");
                    int old = Integer.parseInt(obj.readLine());

                    System.out.println("new value (int): ");
                    int newValue = Integer.parseInt(obj.readLine());

                    dsm.checkAndReplace(var, old, newValue);
                    //writeAll(dsm);
                }
            }
        } else if (me == 1) {
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("a");

            thread.join();
        } else if (me == 2) {
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("b");

            thread.join();
        }
        MPI.Finalize();
    }
}
