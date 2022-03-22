package com.caterbao.lumos.api.merch.rop.vo;

import com.caterbao.lumos.locals.common.vo.SpecDesVo;

import java.util.List;

public class SkuVo {

    private String id;
    private String cumCode;
    private String barCode;
    private List<SpecDesVo> specDes;
    private float salePrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCumCode() {
        return cumCode;
    }

    public void setCumCode(String cumCode) {
        this.cumCode = cumCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public List<SpecDesVo> getSpecDes() {
        return specDes;
    }

    public void setSpecDes(List<SpecDesVo> specDes) {
        this.specDes = specDes;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }
}
