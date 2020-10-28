package ro.alexpopa;

import ro.alexpopa.model.Matrix;
import ro.alexpopa.runner.NormalThreadRunner;
import ro.alexpopa.runner.ThreadPoolRunner;

public class Main {

    private static final int n1=4;
    private static final int m1=4;
    private static final int n2=4;
    private static final int m2=4;

    private static final int NO_THREADS = 3;
    private static final String APPROACH = "NORMAL";
    private static final String FUNCTION = "Row";

    public static void main(String[] args) {
	// write your code here

        Matrix a = new Matrix(n1, m1);
        Matrix b = new Matrix(n2, m2);

        a.populate();
        b.populate();

        System.out.println(a);
        System.out.println(b);

        if (a.n == b.m){
            Matrix result = new Matrix(a.n, b.m);
            float start = System.nanoTime() / 1_000_000;
            if (APPROACH.equals("POOL")){
                ThreadPoolRunner.run(a,b,result, NO_THREADS, FUNCTION);
            }
            else if (APPROACH.equals("NORMAL")){
                NormalThreadRunner.run(a,b,result, NO_THREADS, FUNCTION);
            }
            else {
                System.err.println("Invalid approach.");
            }
            float end = System.nanoTime() / 1_000_000;

            System.out.println("Time elapsed: " + (end-start)/1000 + " seconds");

        }
        else{
            System.err.println("The matrices can't be multiplied");
        }
    }

}
