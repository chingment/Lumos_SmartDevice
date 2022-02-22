package com.caterbao.lumos.locals.common;

import java.util.ArrayList;
import java.util.List;

public class SpecDesModel {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String GetIdx(List<SpecDesModel> specDess) {
        if (specDess == null)
            return null;
        if (specDess.size() <= 0)
            return null;
        List<String> specDesValues = new ArrayList<>();
        for (SpecDesModel specDes : specDess) {
            specDesValues.add(specDes.getValue());
        }

        return String.join(",", specDesValues);
    }
}
