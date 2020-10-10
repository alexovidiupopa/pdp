package ro.alexpopa.domain;

import java.util.Objects;

public final class Operation {
    public int src;
    public int dest;
    public OperationType type;
    public int amount;
    public long timestamp;

    public Operation(OperationType type, int src, int dest, int sum) {
        this.src = src;
        this.dest = dest;
        this.type = type;
        this.amount = sum;
    }

    public Operation(OperationType type, int src, int dest, int amount, long timestamp) {
        this.src = src;
        this.dest = dest;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return src == operation.src &&
                dest == operation.dest &&
                amount == operation.amount &&
                timestamp == operation.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, amount, timestamp);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "src=" + src +
                ", dest=" + dest +
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
