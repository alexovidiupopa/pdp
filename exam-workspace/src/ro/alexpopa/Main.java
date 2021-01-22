package ro.alexpopa;


import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here
//        Permutations p = new Permutations();
//        p.back(new ArrayList<>(), 2, 3);
//        p.executorService.shutdown();
//        System.out.println(p.cnt.get());
        KCombinations kc = new KCombinations();
        kc.generate(new ArrayList<>(), 3, 5, 2);
        kc.executorService.shutdown();
        System.out.println(kc.cnt.get());
        //BigNumProduct product = new BigNumProduct();
        //System.out.println(product.solve(Arrays.asList(1,2,3,4,5), Arrays.asList(1,2,3,4,5),3));
  //      ScalarProductSimple sps = new ScalarProductSimple();
//        System.out.println(sps.scalarProduct(Arrays.asList(1,2,3,4), Arrays.asList(2,3,4,5),4));
        //ScalarProductTree spt = new ScalarProductTree();
       // System.out.println(spt.scalarProduct(Arrays.asList(1,2,3,4), Arrays.asList(2,3,4,5),4));
        //Convolution c = new Convolution();
        //System.out.println(c.convolution(Arrays.asList(1,2,3),Arrays.asList(1,2,3),4));
    }
}
