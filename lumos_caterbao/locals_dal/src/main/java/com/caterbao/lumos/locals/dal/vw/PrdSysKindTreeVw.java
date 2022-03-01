package com.caterbao.lumos.locals.dal.vw;

import java.util.List;

public class PrdSysKindTreeVw {
    private int id;
    private String name;
    private List<PrdSysKindTreeVw> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PrdSysKindTreeVw> getChildren() {
        return children;
    }

    public void setChildren(List<PrdSysKindTreeVw> children) {
        this.children = children;
    }
}
