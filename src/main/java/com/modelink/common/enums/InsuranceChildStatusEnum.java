package com.modelink.common.enums;

/**
 * 保单状态类型枚举
 */
public enum InsuranceChildStatusEnum {

    犹退(1, "犹退"),
    退保(2, "退保"),
    信息有误撤件(3, "信息有误撤件"),
    未承保逾期件(4, "30日未承保逾期件"),
    质检停单未补录音撤件(5, "质检停单未补录音撤件");

    private int value;
    private String text;

    private InsuranceChildStatusEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(InsuranceChildStatusEnum enumItem : InsuranceChildStatusEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(InsuranceChildStatusEnum enumItem : InsuranceChildStatusEnum.values()){
            if(enumItem.getText().equals(text)){
                return enumItem.getValue();
            }
        }
        return 0;
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
