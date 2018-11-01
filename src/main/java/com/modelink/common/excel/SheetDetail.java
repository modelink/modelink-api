package com.modelink.common.excel;

public class SheetDetail {

    private String name;
    private String freezePane = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFreezePane() {
        return freezePane;
    }

    public void setFreezePane(String freezePane) {
        this.freezePane = freezePane;
    }
}
