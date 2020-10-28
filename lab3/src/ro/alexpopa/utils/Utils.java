package ro.alexpopa.utils;

import ro.alexpopa.model.Matrix;
import ro.alexpopa.model.MatrixException;
import ro.alexpopa.thread.ColumnThread;
import ro.alexpopa.thread.KThread;
import ro.alexpopa.thread.MatrixThread;
import ro.alexpopa.thread.RowThread;

public final class Utils {

    public static int buildElement(Matrix a, Matrix b, int row, int col) throws MatrixException {
        if (row<a.n && col<b.m){
            int element = 0;
            for (int i=0;i<a.m;i++){
                element+=a.get(row,i) * b.get(i,col);
            }
            return element;
        }
        else
            throw new MatrixException("Row/column out of bounds!");
    }

    public static MatrixThread createRowThread(
            int index, Matrix a, Matrix b, Matrix c, int noThreads) {
        int numberOfElements = c.n * c.m;
        int count = numberOfElements / noThreads;
        if (index == noThreads - 1)
            count += numberOfElements % noThreads;

        int iStart = count * index / c.n;
        int jStart = count * index % c.n;

        return new RowThread(iStart, jStart, count, a, b, c);
    }

    public static MatrixThread createColumnThread(
            int index, Matrix a, Matrix b, Matrix c, int noThreads) {
        int numberOfElements = c.n * c.m;
        int count = numberOfElements / noThreads;
        if (index == noThreads - 1)
            count += numberOfElements % noThreads;

        int iStart = count * index % c.n;
        int jStart = count * index / c.n;
        return new ColumnThread(iStart, jStart, count, a, b, c);
    }

    public static MatrixThread createKthThread(
            int index, Matrix a, Matrix b, Matrix c, int noThreads) {
        int numberOfElements = c.n * c.m;
        int count = numberOfElements/noThreads;
        if (index< numberOfElements % noThreads) count++;
        int iStart = index / c.m;
        int jStart = index % c.m;
        return new KThread(iStart, jStart,count,
                noThreads, a, b, c);
    }
    
    
}
