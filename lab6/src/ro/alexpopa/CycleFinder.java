package ro.alexpopa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class CycleFinder implements Runnable{
    private final DirectedGraph graph;
    private final int startingNode;
    private final List<Integer> path;
    private final Lock lock;
    private final List<Integer> result;

    CycleFinder(DirectedGraph graph, int node, List<Integer> result) {
        this.graph = graph;
        this.startingNode = node;
        this.path = new ArrayList<>();
        this.lock = new ReentrantLock();
        this.result = result;
    }

    @Override
    public void run() {
        visit(startingNode);
    }

    private void visit(int node) {
        path.add(node);

        if (path.size() == graph.size()) {
            if (graph.neighboursOf(node).contains(startingNode)){ //cycle is complete
                this.lock.lock();
                this.result.clear();
                this.result.addAll(this.path);
                this.lock.unlock();
            }
            return;
        }

        graph.neighboursOf(node).forEach(neighbour->{
            if (!this.path.contains(neighbour)){
                visit(neighbour);
            }
        });
    }

    public static void findHamiltonian(DirectedGraph graph, int threadCount) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        List<Integer> result = new ArrayList<>(graph.size());

        for (int i = 0; i < graph.size(); i++){ //check from each node
            pool.submit(new CycleFinder(graph, i, result));
        }

        pool.shutdown();

        pool.awaitTermination(10, TimeUnit.SECONDS);
    }
}
