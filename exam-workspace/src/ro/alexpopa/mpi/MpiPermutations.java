package ro.alexpopa.mpi;

import mpi.MPI;

import java.util.HashSet;
import java.util.List;

public class MpiPermutations {

    public static boolean check(List<Integer> vec){
        return vec.get(0)%2==0;
    }

    public static boolean potential(List<Integer> vec){
        return new HashSet<>(vec).size() == vec.size();
    }
    public void back(int n, List<Integer> buffer){
        if(buffer.size()==n && potential(buffer)){
            System.out.println(buffer.toString());
        }
        else{
            for (int i = 0; i < n; i++) {
                buffer.add(i);
                if (potential(buffer)){
                    back(n,buffer);
                    buffer.remove(buffer.size()-1);
                }
                else{
                    buffer.remove(buffer.size()-1);
                }
            }
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        int n = 10;
        if (me==0){

        }
        else{

        }
        MPI.Finalize();
    }

}
