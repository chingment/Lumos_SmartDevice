package com.caterbao.lumos.locals.common;

public class FieldModel {
    private int value;
    private String text;

    public FieldModel(){

    }

    public  FieldModel(int value,String text) {
        this.value = value;
        this.text = text;
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
