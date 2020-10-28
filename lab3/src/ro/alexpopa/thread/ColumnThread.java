package ro.alexpopa.thread;

import ro.alexpopa.model.Matrix;
import ro.alexpopa.model.Pair;

public class ColumnThread extends MatrixThread{
    public ColumnThread(int iStart, int jStart, int count, Matrix a, Matrix b, Matrix result) {
        super(iStart, jStart, count, a, b, result);

    }

    public void computeElements() {
        int i = iStart, j = jStart;
        int cnt = count;
        while (cnt > 0 &&  i < result.n && j < result.m) {
            pairs.add(new Pair<>(i, j));
            i++;
            cnt--;
            if (i == result.m) {
                i = 0;
                j++;
            }
        }
    }
}
