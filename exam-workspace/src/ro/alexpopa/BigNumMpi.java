package ro.alexpopa;

import mpi.MPI;

import java.util.Arrays;
import java.util.List;

public class BigNumMpi {

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        List<Integer> a = Arrays.asList(1,2,3);
        List<Integer> b = Arrays.asList(1,2,3);
        if (me==0){

        }
        else{

        }
        MPI.Finalize();
    }

}
