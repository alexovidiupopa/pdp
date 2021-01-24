package ro.alexpopa.mpi;

import mpi.MPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MpiMatrixSum {

    private static void master(List<List<Integer>> a, List<List<Integer>> b, int nrProcs){
        List<List<Integer>> c = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < a.get(0).size(); j++) {
                row.add(0);
            }
            c.add(row);
        }

        int n = a.size();
        int toShare = nrProcs - 1;
        int step = n / toShare;
        int remainder = n % toShare;

        int start = 0, stop = 0;

        for (int i = 1; i <= toShare ; i++) {
            stop = start + step;
            if (remainder>0){
                stop++;
                remainder--;
            }

            int[] metadata = new int[]{start, stop};
            MPI.COMM_WORLD.Send(metadata, 0, 2, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{a},0,1,MPI.OBJECT,i,0);
            MPI.COMM_WORLD.Send(new Object[]{b},0,1,MPI.OBJECT,i,0);


            start = stop;
        }
        for (int i = 1; i <= toShare ; i++) {
            Object[] ans = new Object[1];
            int[] metadata = new int[2];
            MPI.COMM_WORLD.Recv(metadata,0,2,MPI.INT,i,0);
            MPI.COMM_WORLD.Recv(ans, 0, 1, MPI.OBJECT, i, 0);
            List<List<Integer>> answer = (List<List<Integer>>) ans[0];
            start = metadata[0];
            stop = metadata[1];
            for (int j = start; j < stop; j++) {
                for (int k = 0; k < answer.get(0).size(); k++) {
                    c.get(j).set(k, c.get(j).get(k) + answer.get(j).get(k));
                }

            }
        }

        System.out.println(c.toString());
    }

    private static void worker(int me, int nrProcs){
        int[] metadata = new int[2];
        List<List<Integer>> a,b;
        Object[] objs = new Object[2];
        MPI.COMM_WORLD.Recv(metadata,0,2,MPI.INT,0,0);
        MPI.COMM_WORLD.Recv(objs,0,1,MPI.OBJECT,0,0);
        MPI.COMM_WORLD.Recv(objs,1,1,MPI.OBJECT,0,0);
        a = (List<List<Integer>>)objs[0];
        b = (List<List<Integer>>)objs[1];

        List<List<Integer>> c = new ArrayList<>(a);
        for (int i = 0; i < a.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < a.get(0).size(); j++) {
                row.add(0);
            }
            c.add(row);
        }

        int start = metadata[0], stop = metadata[1];
        for (int i = start; i < stop; i++) {
            for (int j = 0; j < a.get(0).size(); j++) {
                c.get(i).set(j, a.get(i).get(j) + b.get(i).get(j));
            }

        }
        MPI.COMM_WORLD.Send(metadata,0,2,MPI.INT, 0,0);
        MPI.COMM_WORLD.Send(new Object[]{c},0,1,MPI.OBJECT, 0,0);
    }


    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        List<List<Integer>> a = Arrays.asList(Arrays.asList(1,2,3),Arrays.asList(3,4,5), Arrays.asList(4,5,6));
        List<List<Integer>> b = Arrays.asList(Arrays.asList(1,2,3),Arrays.asList(3,4,5), Arrays.asList(4,5,6));
        if (me==0){
            master(a,b,nrProcs);
        }
        else{
            worker(me, nrProcs);
        }
        MPI.Finalize();
    }
}
