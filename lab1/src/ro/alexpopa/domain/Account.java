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
        mtx = new ReentrantLock();
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
        logTransfer(OperationType.SEND,sum);
        other.logTransfer(OperationType.RECEIVE,sum);

        this.mtx.unlock();
        other.mtx.unlock();

        return true;
    }


    public void logTransfer(OperationType type, int sum){
        log.log(type,sum);
    }

    public boolean check() {
        this.mtx.lock();
        int initialBalance = this.initialBalance;
        for (Operation operation: this.log.operations){
            if (operation.type==OperationType.SEND)
                initialBalance-=operation.amount;
            else
                initialBalance+=operation.amount;
        }
        this.mtx.unlock();
        return initialBalance==this.balance;
    }
}
