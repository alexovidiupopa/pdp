package ro.alexpopa.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Log {
    public List<Operation> operations;

    public Log() {
        operations = new ArrayList<>();
    }

    public void log(OperationType type, int sum, int src, int dest, long timestamp){
        operations.add(new Operation(type,src, dest,sum,timestamp));
    }

    public void printAllOperations(){
        System.out.println(operations.stream().map(Object::toString).collect(Collectors.joining()));
    }
}
