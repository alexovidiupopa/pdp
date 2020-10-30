package ro.alexpopa.thread;


import ro.alexpopa.model.Matrix;
import ro.alexpopa.model.Pair;


public final class KTask extends MatrixTask {
    public KTask(int iStart, int jStart, int count, int K, Matrix a, Matrix b, Matrix result) {
        super(iStart, jStart, count, a, b, result, K);

    }

    public void computeElements() {
        int i = iStart, j = jStart;
        int size = sizeOfTask;
        while (size > 0 && i < result.n) {
            pairs.add(new Pair<>(i, j));
            size--;
            i += (j + k) / result.m;
            j = (j + k) % result.m;
        }
    }


}
