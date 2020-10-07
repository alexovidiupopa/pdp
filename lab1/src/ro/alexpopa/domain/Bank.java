package ro.alexpopa.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Bank {
    public List<Account> accounts;

    private static final int NO_THREADS = 10;
    private static final int NO_ACCOUNTS = 100;
    private static final long NO_OPERATIONS = 50000 ;


    public Bank() {
        accounts = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Bank{" +
                "accounts=" + accounts +
                '}';
    }

    public void run() {
        createAccounts();

        float start = System.nanoTime() / 1000000;


        List<Thread> threads = new ArrayList<>();
        for (int i=0;i<NO_THREADS;i++){
            int finalI = i;
            threads.add(new Thread(() -> {
                Random r = new Random();
                for(long j = 0; j <NO_OPERATIONS/NO_THREADS; ++j){
                    int accId = r.nextInt(100);
                    int accId2 = r.nextInt(100);
                    if (accId == accId2){
                        --j;
                        continue;
                    }

                    int sum = r.nextInt(25);
                    accounts.get(accId).makeTransfer(accounts.get(accId2),sum);
                    //System.out.println("[Thread " + finalI + "]:" + sum + " were transferred from acc" + accId + " to acc " + accId2);


                    if (r.nextInt(9)==0){
                        runCorrectnessCheck();
                    }
                }
            }));
        }

        threads.forEach(Thread::start);

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        runCorrectnessCheck();

        float end = System.nanoTime() / 1000000;

        System.out.println("Time elapsed: " + (end-start)/1000 + " seconds");

    }

    private void createAccounts(){
        int uid = 0;
        for (int i=0;i<NO_ACCOUNTS;++i){
            accounts.add(new Account(uid++,200));
        }
    }

    private void runCorrectnessCheck() {
        int failedAccounts = 0;
        for (Account account:accounts){
            if (!account.check()){
                failedAccounts++;
            }
        }
        if (failedAccounts>0){
            //throw new RuntimeException("Accounts are no longer correct and consistent");
        }
    }
}
