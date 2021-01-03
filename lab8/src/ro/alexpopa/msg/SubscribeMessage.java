package ro.alexpopa.msg;

import java.io.Serializable;

public class SubscribeMessage extends Message implements Serializable  {

    public String var;
    public int rank;

    public SubscribeMessage(String var, int rank) {
        this.var = var;
        this.rank = rank;
    }
}
