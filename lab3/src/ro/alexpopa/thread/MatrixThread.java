package ro.alexpopa.thread;


import ro.alexpopa.model.Matrix;
import ro.alexpopa.model.MatrixException;
import ro.alexpopa.model.Pair;
import ro.alexpopa.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class MatrixThread extends Thread {

    public List<Pair<Integer,Integer>> pairs;
    public final int iStart, jStart, count;
    public final Matrix a,b, result;
    public int k;

    public MatrixThread(int iStart, int jStart, int count, Matrix a, Matrix b, Matrix result, int k) {
        this.iStart = iStart;
        this.jStart = jStart;
        this.count = count;
        this.a = a;
        this.b = b;
        this.result = result;
        this.k = k;
        this.pairs = new ArrayList<>();
        computeElements();
    }

    public MatrixThread(int iStart, int jStart, int count, Matrix a, Matrix b, Matrix result) {
        this.iStart = iStart;
        this.jStart = jStart;
        this.count = count;
        this.a = a;
        this.b = b;
        this.result = result;
        this.pairs = new ArrayList<>();
        computeElements();
    }


    public abstract void computeElements();

    @Override
    public void run() {
        for (Pair<Integer,Integer> p: pairs){
            int i = p.first;
            int j = p.second;
            try {
                result.set(i,j, Utils.buildElement(a,b,i,j));
            } catch (MatrixException e) {
                e.printStackTrace();
            }
        }
    }
}
