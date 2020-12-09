package ro.alexpopa;

import mpi.MPI;

import java.util.Arrays;

public class Main {


    private static void multiplySimpleMaster(Polynomial p, Polynomial q, int nrProcs) {
        long startTime = System.currentTimeMillis();
        int start = 0, finish = 0;
        int len = p.getLength() / (nrProcs-1);

        for(int i=1;i<nrProcs;i++){
            start=finish;
            finish+=len;
            if (i==nrProcs-1){
                finish = p.getLength();
            }
            MPI.COMM_WORLD.Send(new Object[]{p},0,1,MPI.OBJECT,i,0);
            MPI.COMM_WORLD.Send(new Object[]{q},0,1,MPI.OBJECT,i,0);

            MPI.COMM_WORLD.Send(new int[]{start}, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{finish}, 0, 1, MPI.INT, i, 0);

        }

        Object[] results = new Object[nrProcs-1];
        for(int i=1;i<nrProcs;i++){
            MPI.COMM_WORLD.Recv(results,i-1,1,MPI.OBJECT,i,0);
        }
        Polynomial result = Operation.addUp(results);
        long endTime = System.currentTimeMillis();
        System.out.println("Simple multiplication of polynomials: " + result.toString());
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }

    private static void multiplyKaratsubaMaster(Polynomial p, Polynomial q, int nrProcs) {
        long startTime = System.currentTimeMillis();

        int n = p.getCoefficients().size();
        for(int i=1;i<n;i++){
            int begin = i*n / nrProcs;
            int end = Math.min(n, (i+1) * n / nrProcs);

        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }


    private static void multiplySimpleWorker() {
        Object[] p = new Object[2];
        Object[] q = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p,0,1,MPI.OBJECT, 0,0);
        MPI.COMM_WORLD.Recv(q,0,1,MPI.OBJECT, 0,0);

        MPI.COMM_WORLD.Recv(begin,0,1,MPI.INT, 0,0);
        MPI.COMM_WORLD.Recv(end,0,1,MPI.INT, 0,0);

        Polynomial result = Operation.multiplySimple(p[0],q[0],begin[0],end[0]);

        MPI.COMM_WORLD.Send(new Object[]{result},0,1,MPI.OBJECT,0,0);

    }

    private static void multiplyKaratsubaWorker() {
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        if (me==0){
            // master process
            Polynomial p = new Polynomial(5);
            Polynomial q = new Polynomial(5);

            System.out.println(p);
            System.out.println(q);

            multiplySimpleMaster(p,q, nrProcs);
            multiplyKaratsubaMaster(p,q, nrProcs);
        }
        else {
            multiplySimpleWorker();

            multiplyKaratsubaWorker();
        }
        MPI.Finalize();
    }


}
