package ro.alexpopa.domain;

public final class Operation {
    public long number;
    public OperationType type;
    public int amount;

    public Operation(OperationType type, long number, int sum) {
        this.number = number;
        this.type = type;
        this.amount = sum;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "number=" + number +
                ", type=" + type +
                ", amount=" + amount +
                '}';
    }
}
