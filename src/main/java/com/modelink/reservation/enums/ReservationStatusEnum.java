package com.modelink.reservation.enums;

/**
 * 预约状态枚举
 */
public enum ReservationStatusEnum {

    CREATED(0, "待沟通"),
    FINISHING(1, "沟通中"),
    FINISHED_SUCC(2, "已沟通_成功"),
    FINISHED_FAIL(3, "已沟通_失败");

    private int value;
    private String text;
    private ReservationStatusEnum(int value, String text){
        this.text = text;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
