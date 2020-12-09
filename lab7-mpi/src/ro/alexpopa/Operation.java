package ro.alexpopa;

import java.util.ArrayList;
import java.util.List;

public class Operation {
    public static Polynomial multiplySimple(Object o, Object o1, int begin, int end) {
        Polynomial p = (Polynomial) o;
        Polynomial q = (Polynomial) o1;
        List<Integer> zeros = new ArrayList<>();
        for (int i = 0; i < p.getDegree() * 2 + 1; i++)
            zeros.add(0);
        Polynomial result = new Polynomial(zeros);
        for (int i = begin; i < end; i++) {
            for (int j = 0; j < q.getCoefficients().size(); j++) {
                result.getCoefficients().set(i + j, result.getCoefficients().get(i + j) + p.getCoefficients().get(i) * q.getCoefficients().get(j));
            }
        }
        return result;
    }

    public static Polynomial addUp(Object[] results) {
        int degree = ((Polynomial) results[0]).getDegree();
        List<Integer> zeros = new ArrayList<>();
        for (int i = 0; i < degree + 1; i++)
            zeros.add(0);
        Polynomial result = new Polynomial(zeros);
        for (int i = 0; i < result.getCoefficients().size(); i++) {
            for (Object o : results) {
                result.getCoefficients().set(i, result.getCoefficients().get(i) + ((Polynomial) o).getCoefficients().get(i));
            }
        }
        return result;
    }

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
        Polynomial z2 = karatsubaSequential(Polynomial.add(lowP1, highP1), Polynomial.add(lowP2, highP2));
        Polynomial z3 = karatsubaSequential(highP1, highP2);

        //calculate the final result
        Polynomial r1 = Polynomial.addZeros(z3, 2 * len);
        Polynomial r2 = Polynomial.addZeros(Polynomial.subtract(Polynomial.subtract(z2, z3), z1), len);
        Polynomial result = Polynomial.add(Polynomial.add(r1, r2), z1);
        return result;
    }

}
