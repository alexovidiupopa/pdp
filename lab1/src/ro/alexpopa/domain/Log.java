package ro.alexpopa.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Log {
    public List<Operation> operations;

    public Log() {
        operations = new ArrayList<>();
    }

    public void log(OperationType type, int sum){
        operations.add(new Operation(type,operations.size(),sum));
    }

    public void printAllOperations(){
        System.out.println(operations.stream().map(Object::toString).collect(Collectors.joining()));
    }
}
