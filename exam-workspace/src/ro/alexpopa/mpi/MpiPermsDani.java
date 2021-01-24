package ro.alexpopa.mpi;

import mpi.MPI;

import java.util.ArrayList;
import java.util.List;

public class MpiPermsDani {
    private static void master(int n, int numberOfProcesses) {
        List<Integer> solution = new ArrayList<>();
        back(solution, n, 0, numberOfProcesses);

    }


    private static void back(List<Integer> solution, int n, int me, int numberOfProcesses) {
//        System.out.println("doing back on [] " + solution.toString());
        if (solution.size() == n) {
            if (solution.get(0) == 1) {
                System.out.println("solution: " + solution.toString());
            }
            return;
        }
        if (numberOfProcesses >= 2) {
            int child = me + numberOfProcesses / 2;
            List<Integer> toSend = new ArrayList<>(solution);
//            System.out.println("master = parent is: " + me + " child is " + child + " [] " + toSend.toString());
            MPI.COMM_WORLD.Send(new Object[]{toSend}, 0, 1, MPI.OBJECT, child, 0);
            List<Integer> temp = new ArrayList<>(solution);
//            System.out.println("master = parent is " + me + " local sort " + "[]" + temp.toString());
            for (int i = 0; i < n; i += 2) {
                if (temp.contains(i)) continue;
                temp.add(i);
                back(temp, n, me, numberOfProcesses / 2);
                temp.remove(temp.size() - 1);
            }
        } else {
            for (int i = 0; i < n; i++) {
                if (solution.contains(i)) continue;
                solution.add(i);
                back(solution, n, me, 1);
                solution.remove(solution.size() - 1);
            }
        }
    }

    private static void worker(int n, int me, int numberOfProcesses) {
        int base = 0;
        int parent = -1;
        int offset = me;
        while (offset > 0) {
            parent = base;
            int mid = numberOfProcesses / 2;
            if (offset < mid) {
                numberOfProcesses = numberOfProcesses / 2;
            } else {
                offset = offset - mid;
                numberOfProcesses = numberOfProcesses / 2 + numberOfProcesses % 2;
                base += mid;
            }
        }
        Object[] receivedData = new Object[1];
        MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, parent, 0);
        List<Integer> array = (List<Integer>) receivedData[0];
//        System.out.println(me + " child received [] " + array.toString());
        for (int i = 1; i < n; i += 2) {
            if (array.contains(i)) continue;
            array.add(i);
            back(array, n, me, numberOfProcesses);
            array.remove(array.size() - 1);
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int selfRank = MPI.COMM_WORLD.Rank();
        int numberOfProcesses = MPI.COMM_WORLD.Size();
        int n = 4;
        if (selfRank == 0) {
            master(n, numberOfProcesses);
        } else {
            worker(n, selfRank, numberOfProcesses);
        }
        MPI.Finalize();
    }

}
