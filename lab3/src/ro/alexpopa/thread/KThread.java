package ro.alexpopa.thread;


import ro.alexpopa.model.Matrix;
import ro.alexpopa.model.Pair;


public class KThread extends MatrixThread{
    public KThread(int iStart, int jStart, int count, int K, Matrix a, Matrix b, Matrix result) {
        super(iStart, jStart, count, a, b, result, K);

    }

    public void computeElements() {
        int i = iStart, j = jStart;
        int cnt = count;
        while (cnt > 0 && i < result.n && j < result.m) {
            pairs.add(new Pair<>(i, j));
            cnt--;
            i += (j + k) / result.m;
            j = (j + k) % result.m;
        }
    }


}
