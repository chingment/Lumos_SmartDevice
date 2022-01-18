package com.caterbao.lumos.api.merch.rop.model;

public class FieldModel {
    private String text;
    private int value;

    public FieldModel(){

    }

    public FieldModel(int value,String text){
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
