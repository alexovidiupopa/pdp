package ro.alexpopa;

public class Main {

    private static final int NR_GRAPHS = 50;
    private static final int NR_THREADS = 5;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= NR_GRAPHS; i++) {
            DirectedGraph graph = DirectedGraph.generateRandomHamiltonian(i*10); // nr of vertices
            test(i, graph, NR_THREADS);
        }
    }

    public static void test(int vertices, DirectedGraph graph, int threadCount) throws InterruptedException {
        long startTime = System.nanoTime();
        CycleFinder.findHamiltonian(graph, threadCount);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println(vertices*10 + " vertices: " + duration + " ms");
    }


}
