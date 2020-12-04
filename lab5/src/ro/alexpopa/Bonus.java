package ro.alexpopa;

import java.math.BigInteger;
import java.util.Random;

public class Bonus {

    private static BigInteger generate(int digits){
        StringBuilder number = new StringBuilder();
        Random r = new Random();
        for (int i=0;i<digits;i++){
            number.append(r.nextInt(10));
        }
        return new BigInteger(number.toString());
    }

    public static void main(String[] args) {
        int digits = 5;
        BigInteger a = generate(digits);
        BigInteger b = generate(digits);
        System.out.println(a);
        System.out.println(b);
        BigInteger res = multiplyKaratsubaBig(a,b);
        System.out.println(res);
    }

    private static BigInteger multiplyKaratsubaBig(BigInteger x, BigInteger y) {
        int len = Math.min(x.toString().length(), y.toString().length());
        if (len<5)
            return x.multiply(y);

        len/=2;

        String xs = x.toString();
        String ys = y.toString();

        BigInteger high1 = new BigInteger(xs.substring(0, xs.length()-len));
        BigInteger low1 = new BigInteger(xs.substring(xs.length()-len));
        BigInteger high2 = new BigInteger(ys.substring(0, ys.length()-len));
        BigInteger low2 = new BigInteger(ys.substring(ys.length()-len));

        BigInteger z1 = multiplyKaratsubaBig(low1, low2);
        BigInteger z2 = multiplyKaratsubaBig(low1.add(high1), low2.add(high2));
        BigInteger z3 = multiplyKaratsubaBig(high1, high2);

        BigInteger r1 = addZeros(z3, 2*len);
        BigInteger r2 = addZeros(z2.subtract(z3).subtract(z1), len);
        BigInteger result = r1.add(r2).add(z1);

        return result;
    }

    private static BigInteger addZeros(BigInteger num, int offset){
        StringBuilder res = new StringBuilder();
        res.append(num.toString());
        for (int i=0; i<offset;i++){
            res.append("0");
        }

        return new BigInteger(res.toString());
    }
}
