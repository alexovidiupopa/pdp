package ro.alexpopa.msg;

import java.io.Serializable;

public class ChangeMessage implements Serializable {

    public String var;
    public int oldVal, newVal;

    public ChangeMessage(String var, int oldVal, int newVal) {
        this.var = var;
        this.oldVal = oldVal;
        this.newVal = newVal;
    }
}
