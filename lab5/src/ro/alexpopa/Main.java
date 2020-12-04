package ro.alexpopa;

import ro.alexpopa.model.Operation;
import ro.alexpopa.model.Polynomial;

import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
	// write your code here
        Polynomial p = new Polynomial(100);
        Polynomial q = new Polynomial(100);

        System.out.println("P:" + p);
        System.out.println("Q:" + q);
        System.out.println("\n");

        //Simple
        System.out.println(simpleSequential(p, q).toString() + "\n");
        System.out.println(simpleThreaded(p, q).toString() + "\n");

        //Karatsuba
        System.out.println(karatsubaSequential(p,q).toString() + "\n");
        System.out.println(karatsubaThreaded(p,q).toString() + "\n");

    }

    private static Polynomial simpleSequential(Polynomial p, Polynomial q) {
        long startTime = System.currentTimeMillis();
        Polynomial result1 = Operation.simpleSequential(p, q);
        long endTime = System.currentTimeMillis();
        System.out.println("Simple sequential multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result1;
    }

    private static Polynomial simpleThreaded(Polynomial p, Polynomial q) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Polynomial result2 = Operation.simpleThreaded(p, q);
        long endTime = System.currentTimeMillis();
        System.out.println("Simple parallel multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result2;
    }

    private static Polynomial karatsubaSequential(Polynomial p, Polynomial q) {
        long startTime = System.currentTimeMillis();
        Polynomial result3 = Operation.karatsubaSequential(p, q);
        long endTime = System.currentTimeMillis();
        System.out.println("Karatsuba sequential multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result3;
    }

    private static Polynomial karatsubaThreaded(Polynomial p, Polynomial q) throws ExecutionException,
            InterruptedException {
        long startTime = System.currentTimeMillis();
        Polynomial result4 = Operation.karatsubaThreaded(p, q, 4);
        long endTime = System.currentTimeMillis();
        System.out.println("Karatsuba parallel multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result4;
    }



}
