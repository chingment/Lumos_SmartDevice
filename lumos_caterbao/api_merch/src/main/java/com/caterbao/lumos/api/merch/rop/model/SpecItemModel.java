package com.caterbao.lumos.api.merch.rop.model;

import java.util.List;

public class SpecItemModel {
    private String name;
    private List<SpecItemValueModel> value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecItemValueModel> getValue() {
        return value;
    }

    public void setValue(List<SpecItemValueModel> value) {
        this.value = value;
    }
}
