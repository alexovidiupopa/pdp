package ro.alexpopa.domain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class Account {
    public int uid;
    public int balance;
    public Log log;
    public int initialBalance;

    public Lock mtx;


    public Account(int uid, int balance) {
        this.uid=uid;
        this.initialBalance = balance;
        this.balance = balance;
        this.mtx = new ReentrantLock();
        this.log = new Log();
    }

    public boolean makeTransfer(Account other, int sum){
        if (sum>balance){
            return false;
        }

        if (this.uid<other.uid){
            this.mtx.lock();
            other.mtx.lock();
        }
        else {
            other.mtx.lock();
            this.mtx.lock();
        }

        balance-=sum;
        other.balance+=sum;
        long timestamp = System.currentTimeMillis();
        logTransfer(OperationType.SEND,this.uid, other.uid,sum, timestamp);
        other.logTransfer(OperationType.RECEIVE,other.uid, this.uid, sum, timestamp);

        this.mtx.unlock();
        other.mtx.unlock();

        return true;
    }


    public void logTransfer(OperationType type, int src, int dest, int sum, long timestamp){
        log.log(type,sum, src, dest, timestamp);
    }

    public boolean check() {
        int initBalance = this.initialBalance;
        for (Operation operation: this.log.operations){
            if (operation.type==OperationType.SEND)
                initBalance-=operation.amount;
            else
                initBalance+=operation.amount;
        }
        return initBalance==this.balance;
    }


}
