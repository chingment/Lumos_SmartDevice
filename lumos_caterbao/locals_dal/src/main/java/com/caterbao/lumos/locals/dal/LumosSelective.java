package com.caterbao.lumos.locals.dal;

import java.util.HashMap;
import java.util.List;

public class LumosSelective {
    private String fields;
    private HashMap<String,Object> where;

    public LumosSelective() {
        this.where = new HashMap<>();
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public HashMap<String, Object> getWhere() {
        return where;
    }

    public void setWhere(HashMap<String, Object> where) {
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

    public void addWhere(String key,String[] val){
        if(where==null) {
            where = new HashMap<>();
        }
        if(!where.containsKey(key)){
            where.put(key,val);
        }
    }

    public void addWhere(String key, List<String> val){
        if(where==null) {
            where = new HashMap<>();
        }
        if(!where.containsKey(key)){
            where.put(key,val);
        }
    }
}
