package ro.alexpopa;

import mpi.MPI;
import ro.alexpopa.msg.DSM;
import ro.alexpopa.msg.Message;

import static ro.alexpopa.Main.writeAll;

public class Listener implements Runnable {
    private DSM dsm;

    public Listener(DSM dsm) {
        this.dsm = dsm;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " waiting..");
            Object[] messages = new Object[1];

            MPI.COMM_WORLD.Recv(messages,0, 1, MPI.OBJECT, MPI.ANY_SOURCE, MPI.ANY_TAG);
            Message message = (Message)messages[0];
            if (message.done)
                return;
            if (message.updateMessage!=null){
                System.out.println("Update message received");
                System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " received:" + message.updateMessage.var + "->" + message.updateMessage.val);
                dsm.setVariable(message.updateMessage.var, message.updateMessage.val);
            }
            if (message.subscribeMessage!=null){
                System.out.println("Subscribe message received");
                System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " received:" + message.subscribeMessage.var + "->" + message.subscribeMessage.rank);
                dsm.subscribeOther(message.subscribeMessage.var, message.subscribeMessage.rank);
            }
            writeAll(dsm);
        }
    }
}
