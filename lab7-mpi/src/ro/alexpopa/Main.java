package ro.alexpopa;

import mpi.MPI;

public class Main {

    private static final String MULTIPLICATION = "Karatsuba";
    private static final int POLYNOMIAL_ORDER = 5;

    private static void multiplicationMaster(Polynomial p, Polynomial q, int nrProcs, String type) {
        long startTime = System.currentTimeMillis();
        int start = 0, finish = 0;
        int len = p.getLength() / (nrProcs - 1);

        for (int i = 1; i < nrProcs; i++) {
            start = finish;
            finish += len;
            if (i == nrProcs - 1) {
                finish = p.getLength();
            }
            MPI.COMM_WORLD.Send(new Object[]{p}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{q}, 0, 1, MPI.OBJECT, i, 0);

            MPI.COMM_WORLD.Send(new int[]{start}, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{finish}, 0, 1, MPI.INT, i, 0);

        }

        Object[] results = new Object[nrProcs - 1];
        for (int i = 1; i < nrProcs; i++) {
            MPI.COMM_WORLD.Recv(results, i - 1, 1, MPI.OBJECT, i, 0);
        }

        Polynomial result = Operation.buildResult(results);
        long endTime = System.currentTimeMillis();
        System.out.println(type + " multiplication of polynomials:\n" + result.toString());
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }

    private static void multiplySimpleWorker(int me) {
        System.out.printf("Worker %d started\n", me);

        Object[] p = new Object[2];
        Object[] q = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(q, 0, 1, MPI.OBJECT, 0, 0);

        MPI.COMM_WORLD.Recv(begin, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial result = Operation.multiplySimple(p[0], q[0], begin[0], end[0]);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);

    }

    private static void multiplyKaratsubaWorker(int me) {
        System.out.printf("Worker %d started\n", me);

        Object[] p = new Object[2];
        Object[] q = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(q, 0, 1, MPI.OBJECT, 0, 0);

        MPI.COMM_WORLD.Recv(begin, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial pp = (Polynomial) p[0];
        Polynomial qq = (Polynomial) q[0];

        for (int i = 0; i < begin[0]; i++) {
            pp.getCoefficients().set(i, 0);
        }
        for (int j = end[0]; j < pp.getCoefficients().size(); j++) {
            pp.getCoefficients().set(j, 0);

        }

        Polynomial result = Operation.karatsubaSequential(pp, qq);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        if (me == 0) {
            // master process
            System.out.println("Master process generating polynomials:");
            Polynomial p = new Polynomial(POLYNOMIAL_ORDER);
            Polynomial q = new Polynomial(POLYNOMIAL_ORDER);

            System.out.println(p);
            System.out.println(q);

            multiplicationMaster(p, q, nrProcs, MULTIPLICATION);
        } else {
            if ("Karatsuba".compareTo(MULTIPLICATION) == 0){
                multiplyKaratsubaWorker(me);
            }
            else{
                multiplySimpleWorker(me);
            }
        }
        MPI.Finalize();
    }


}
