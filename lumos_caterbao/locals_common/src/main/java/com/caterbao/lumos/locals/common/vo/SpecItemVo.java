package com.caterbao.lumos.locals.common.vo;

import java.util.List;

public class SpecItemVo {
    private String name;
    private List<SpecItemValueVo> value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecItemValueVo> getValue() {
        return value;
    }

    public void setValue(List<SpecItemValueVo> value) {
        this.value = value;
    }
}
