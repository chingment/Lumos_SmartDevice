package com.caterbao.lumos.locals.common;

public class FieldModel {
    private Object value;
    private String text;

    public FieldModel(){

    }

    public  FieldModel(Object value,String text) {
        this.value = value;
        this.text = text;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
