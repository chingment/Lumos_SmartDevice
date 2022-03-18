package com.caterbao.lumos.api.merch.rop.vo;

public class FieldVo {
    private String text;
    private int value;

    public FieldVo(){

    }

    public FieldVo(int value, String text){
        this.value=value;
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
