package com.modelink.common.enums;

/**
 * 平台枚举
 */
public enum TransformCycleEnum {

    from_1(1, "1天"),
    from_2(2, "2天"),
    from_3(3, "3天"),
    from_4(4, "4天"),
    from_5(5, "5天"),
    from_6(6, "6天"),
    from_7_14(7, "7天-14天"),
    from_15_30(15, "15天-30天"),
    from_31_60(31, "31天-60天"),
    from_61_90(61, "61天-90天"),
    from_91(91, "大于90天");

    private int value;
    private String text;

    private TransformCycleEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(TransformCycleEnum enumItem : TransformCycleEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(TransformCycleEnum enumItem : TransformCycleEnum.values()){
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
