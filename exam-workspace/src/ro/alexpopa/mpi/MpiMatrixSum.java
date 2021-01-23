package ro.alexpopa.mpi;

import mpi.MPI;

public class MpiMatrixSum {

    private static void master(){

    }

    private static void worker(){

    }
    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        if (me==0){

        }
        else{

        }
        MPI.Finalize();
    }
}
