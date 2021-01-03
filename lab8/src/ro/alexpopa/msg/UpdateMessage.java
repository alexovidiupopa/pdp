package ro.alexpopa.msg;

import java.io.Serializable;

public class UpdateMessage extends Message implements Serializable {

    public String var;
    public int val;

    public UpdateMessage(String var, int val) {
        this.var = var;
        this.val = val;
    }
}
