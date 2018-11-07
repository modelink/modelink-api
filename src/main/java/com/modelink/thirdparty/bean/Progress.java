package com.modelink.thirdparty.bean;

import java.io.Serializable;

public class Progress implements Serializable {

    private int present;
    private int total;

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
