package ro.alexpopa;

import java.util.List;

public class Hamiltonian {

    public boolean solved = false;
    public static int V = 5;

    public boolean isSafe(int v, boolean graph[][], List<Integer> path, int pos){
        if (!graph[path.get(pos-1)][v])
            return false;
        return true;
    }

}
