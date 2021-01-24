package ro.alexpopa.mpi;

import mpi.MPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MpiMergeSort {
    private static void master(List<Integer> array, int numberOfProcesses) {
        List<Integer> solution = mergeSortUtil(array, 0, numberOfProcesses);
        System.out.println("--------------");
        solution.forEach(s -> System.out.print(s + " "));
    }

    private static List<Integer> mergeSortUtil(List<Integer> array, int me, int numberOfProcesses) {
//        System.out.println("received to sort util: " + array.toString());
        if (array.size() <= 1) return array;
        List<Integer> result1, result2;
        if (numberOfProcesses >= 2) {
            int child = me + numberOfProcesses / 2;
            List<Integer> toSend = new ArrayList<>(array.subList(0, array.size() / 2));
//            System.out.println("master = parent is: " + me + " child is " + child + " [] " + toSend.toString());
            MPI.COMM_WORLD.Send(new Object[]{toSend}, 0, 1, MPI.OBJECT, child, 0);
//            System.out.println("master = parent is " + me + " local sort " + "[]" + array.subList(array.size() / 2, array.size()).toString());
            result2 = mergeSortUtil(array.subList(array.size() / 2, array.size()), me, numberOfProcesses / 2);
            Object[] receivedData = new Object[1];
            MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, child, 0);
            result1 = (List<Integer>) receivedData[0];
        } else {
            result1 = mergeSortUtil(array.subList(0, array.size() / 2), me, 1);
            result2 = mergeSortUtil(array.subList(array.size() / 2, array.size()), me, 1);
        }
        return merge(result1, result2);
    }

    private static List<Integer> merge(List<Integer> list1, List<Integer> list2) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i) < list2.get(j)) {
                result.add(list1.get(i));
                i++;
            } else {
                result.add(list2.get(j));
                j++;
            }
        }
        result.addAll(list1.subList(i, list1.size()));
        result.addAll(list2.subList(j, list2.size()));
        return result;
    }

    private static void worker(int n, int me, int numberOfProcesses) {
        int base = 0;
        int parent = -1;
        int offset = me;
        while(offset > 0) {
            parent = base;
            int mid = numberOfProcesses / 2;
            if(offset < mid) {
                n = n / 2;
                numberOfProcesses = numberOfProcesses / 2;
            } else {
                offset = offset - mid;
                n = n / 2 + n % 2;
                numberOfProcesses = numberOfProcesses / 2 + numberOfProcesses % 2;
                base += mid;
            }
        }

        Object[] receivedData = new Object[1];
        MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, parent, 0);
        List<Integer> array = (List<Integer>) receivedData[0];
//        System.out.println("child = child is " + me + " parent is " + parent + " [] " + array.toString());
        array = mergeSortUtil(array, me, numberOfProcesses);

        MPI.COMM_WORLD.Send(new Object[]{array}, 0, 1, MPI.OBJECT, parent, 0);
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int selfRank = MPI.COMM_WORLD.Rank();
        int numberOfProcesses = MPI.COMM_WORLD.Size();
        List<Integer> array = Arrays.asList(7, 2, 5, 6, 9, 218, 1, 2, 3, 4, 2, 5, 8, 12, 8, 2, 3, 8);
        if (selfRank == 0) {
            master(array, numberOfProcesses);
        } else {
            worker(array.size(), selfRank, numberOfProcesses);
        }
        MPI.Finalize();

    }

}
