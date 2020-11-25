package ro.alexpopa.model;

public final class Task implements Runnable {
    private final int start;
    private final int end;
    private final Polynomial p1;
    private final Polynomial p2;
    private final Polynomial result;

    public Task(int start, int end, Polynomial p1, Polynomial p2, Polynomial result) {
        this.start = start;
        this.end = end;
        this.p1 = p1;
        this.p2 = p2;
        this.result = result;
    }

    @Override
    public void run() {
        for (int index = start; index < end; index++) {
            //case - no more elements to calculate
            if (index > result.getLength()) {
                return;
            }
            //find all the pairs that we add to obtain the value of a result coefficient
            for (int j = 0; j <= index; j++) {
                if (j < p1.getLength() && (index - j) < p2.getLength()) {
                    int value = p1.getCoefficients().get(j) * p2.getCoefficients().get(index - j);
                    result.getCoefficients().set(index, result.getCoefficients().get(index) + value);
                }
            }
        }
    }
}
