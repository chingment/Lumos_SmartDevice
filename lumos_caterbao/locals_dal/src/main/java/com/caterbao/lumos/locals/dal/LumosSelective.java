package com.caterbao.lumos.locals.dal;

import java.util.HashMap;

public class LumosSelective {
    private String fields;
    private HashMap<String,String> where;

    public LumosSelective() {
        this.where = new HashMap<>();
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public HashMap<String, String> getWhere() {
        return where;
    }

    public void setWhere(HashMap<String, String> where) {
        this.where = where;
    }

    public void addWhere(String key,String val){
        if(where==null) {
            where = new HashMap<>();
        }
        if(!where.containsKey(key)){
            where.put(key,val);
        }
    }
}
