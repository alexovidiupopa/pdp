package ro.alexpopa.thread;


import ro.alexpopa.model.Matrix;
import ro.alexpopa.model.MatrixException;
import ro.alexpopa.model.Pair;
import ro.alexpopa.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class MatrixTask extends Thread {

    public List<Pair<Integer,Integer>> pairs;
    public final int iStart, jStart, sizeOfTask;
    public final Matrix a, b, result;
    public int k;

    public MatrixTask(int iStart, int jStart, int sizeOfTask, Matrix a, Matrix b, Matrix result, int k) {
        this.iStart = iStart;
        this.jStart = jStart;
        this.sizeOfTask = sizeOfTask;
        this.a = a;
        this.b = b;
        this.result = result;
        this.k = k;
        this.pairs = new ArrayList<>();
        computeElements();
    }

    public MatrixTask(int iStart, int jStart, int sizeOfTask, Matrix a, Matrix b, Matrix result) {
        this.iStart = iStart;
        this.jStart = jStart;
        this.sizeOfTask = sizeOfTask;
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
