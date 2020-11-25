package ro.alexpopa;

public class Main {

    private static final int GRAPHS_COUNT = 101;
    private static final int NR_THREADS = 1;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= GRAPHS_COUNT; i++) {
            DirectedGraph graph = DirectedGraph.generateRandomHamiltonian(i*10); // nr of vertices
            test(i, graph, NR_THREADS);
        }
    }

    public static void test(int level, DirectedGraph graph, int threadCount) throws InterruptedException {
        long startTime = System.nanoTime();
        CycleFinder.findHamiltonian(graph, threadCount);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println(level + " vertices: " + duration + " ms");
    }


}
