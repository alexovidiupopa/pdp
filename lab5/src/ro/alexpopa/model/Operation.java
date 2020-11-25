package ro.alexpopa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Operation {
    public static final int NR_THREADS = 5;

    public static Polynomial simpleSequential(Polynomial p1, Polynomial p2) {
        int sizeOfResultCoefficientList = p1.getDegree() + p2.getDegree() + 1;
        List<Integer> coefficients = new ArrayList<>();
        for (int i = 0; i < sizeOfResultCoefficientList; i++) {
            coefficients.add(0);
        }
        for (int i = 0; i < p1.getCoefficients().size(); i++) {
            for (int j = 0; j < p2.getCoefficients().size(); j++) {
                int index = i + j;
                int value = p1.getCoefficients().get(i) * p2.getCoefficients().get(j);
                coefficients.set(index, coefficients.get(index) + value);
            }
        }
        return new Polynomial(coefficients);
    }


    public static Polynomial simpleThreaded(Polynomial p1, Polynomial p2) throws
            InterruptedException {
        //initialize result polynomial with coefficients = 0
        int sizeOfResultCoefficientList = p1.getDegree() + p2.getDegree() + 1;
        List<Integer> coefficients = IntStream.range(0, sizeOfResultCoefficientList).mapToObj(i -> 0).collect(Collectors.toList());
        Polynomial result = new Polynomial(coefficients);

        //calculate the coefficients

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NR_THREADS);
        int step = result.getLength() / NR_THREADS;
        if (step == 0) {
            step = 1;
        }
        //System.out.println("STEP: " + step);
        int end;
        for (int i = 0; i < result.getLength(); i += step) {
            end = i + step;
            Task task = new Task(i, end, p1, p2, result);
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(50, TimeUnit.SECONDS);

        return result;
    }


    public static Polynomial karatsubaSequential(Polynomial p1, Polynomial p2) {
        if (p1.getDegree() < 2 || p2.getDegree() < 2) {
            return simpleSequential(p1, p2);
        }

        int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polynomial lowP1 = new Polynomial(p1.getCoefficients().subList(0, len));
        Polynomial highP1 = new Polynomial(p1.getCoefficients().subList(len, p1.getLength()));
        Polynomial lowP2 = new Polynomial(p2.getCoefficients().subList(0, len));
        Polynomial highP2 = new Polynomial(p2.getCoefficients().subList(len, p2.getLength()));

        Polynomial z1 = karatsubaSequential(lowP1, lowP2);
        Polynomial z2 = karatsubaSequential(add(lowP1, highP1), add(lowP2, highP2));
        Polynomial z3 = karatsubaSequential(highP1, highP2);

        //calculate the final result
        Polynomial r1 = shift(z3, 2 * len);
        Polynomial r2 = shift(subtract(subtract(z2, z3), z1), len);
        Polynomial result = add(add(r1, r2), z1);
        return result;
    }


    public static Polynomial karatsubaThreaded(Polynomial p1, Polynomial p2, int currentDepth)
            throws ExecutionException, InterruptedException {
        //E impartit deja de 4 ori si pentru ca e recursiv, nu mai împarțim in mai mult pt ca nu încape pe stack
        if (currentDepth > 4) {
            return karatsubaSequential(p1, p2);
        }

        if (p1.getDegree() < 2 || p2.getDegree() < 2) {
            return karatsubaSequential(p1, p2);
        }

        int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polynomial lowP1 = new Polynomial(p1.getCoefficients().subList(0, len));
        Polynomial highP1 = new Polynomial(p1.getCoefficients().subList(len, p1.getLength()));
        Polynomial lowP2 = new Polynomial(p2.getCoefficients().subList(0, len));
        Polynomial highP2 = new Polynomial(p2.getCoefficients().subList(len, p2.getLength()));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Callable<Polynomial> task1 = () -> karatsubaThreaded(lowP1, lowP2, currentDepth + 1);
        Callable<Polynomial> task2 = () -> karatsubaThreaded(Operation.add(lowP1, highP1), Operation
                .add(lowP2, highP2), currentDepth + 1);
        Callable<Polynomial> task3 = () -> karatsubaThreaded(highP1, highP2, currentDepth);

        Future<Polynomial> f1 = executor.submit(task1);
        Future<Polynomial> f2 = executor.submit(task2);
        Future<Polynomial> f3 = executor.submit(task3);

        executor.shutdown();

        Polynomial z1 = f1.get();
        Polynomial z2 = f2.get();
        Polynomial z3 = f3.get();

        executor.awaitTermination(60, TimeUnit.SECONDS);

        //calculate the final result
        Polynomial r1 = shift(z3, 2 * len);
        Polynomial r2 = shift(subtract(subtract(z2, z3), z1), len);
        Polynomial result = add(add(r1, r2), z1);
        return result;
    }

    public static Polynomial shift(Polynomial p, int offset) {
        List<Integer> coefficients = IntStream.range(0, offset).mapToObj(i -> 0).collect(Collectors.toList());
        coefficients.addAll(p.getCoefficients());
        return new Polynomial(coefficients);
    }

    public static Polynomial add(Polynomial p1, Polynomial p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        //Add the 2 polynomials
        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(p1.getCoefficients().get(i) + p2.getCoefficients().get(i));
        }

        addExtraCoefficients(p1, p2, minDegree, maxDegree, coefficients);

        return new Polynomial(coefficients);
    }


    public static Polynomial subtract(Polynomial p1, Polynomial p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        //Subtract the 2 polynomials
        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(p1.getCoefficients().get(i) - p2.getCoefficients().get(i));
        }

        addExtraCoefficients(p1, p2, minDegree, maxDegree, coefficients);

        //remove coefficients starting from biggest power if coefficient is 0

        int i = coefficients.size() - 1;
        while (coefficients.get(i) == 0 && i > 0) {
            coefficients.remove(i);
            i--;
        }

        return new Polynomial(coefficients);
    }

    private static void addExtraCoefficients(Polynomial p1, Polynomial p2, int minDegree, int maxDegree, List<Integer> coefficients) {
        if (minDegree != maxDegree) {
            if (maxDegree == p1.getDegree()) {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(p1.getCoefficients().get(i));
                }
            } else {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(p2.getCoefficients().get(i));
                }
            }
        }
    }

}
