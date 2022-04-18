package com.caterbao.lumos.locals.biz.model;

import com.caterbao.lumos.locals.common.vo.SpecDesVo;

import java.io.Serializable;
import java.util.List;

public class SkuInfo  implements Serializable {
    private String id;
    private String cumCode;
    private String spuId;
    private String name;
    private String barCode;
    private String pyIdx;
    private String imgUrl;
    private String specIdx;
    private Boolean isOffSell;
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

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getPyIdx() {
        return pyIdx;
    }

    public void setPyIdx(String pyIdx) {
        this.pyIdx = pyIdx;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public String getSpecIdx() {
        return specIdx;
    }

    public void setSpecIdx(String specIdx) {
        this.specIdx = specIdx;
    }

    public List<SpecDesVo> getSpecDes() {
        return specDes;
    }

    public void setSpecDes(List<SpecDesVo> specDes) {
        this.specDes = specDes;
    }

    public Boolean getIsOffSell() {
        return isOffSell;
    }

    public void setIsOffSell(Boolean isOffSell) {
        this.isOffSell = isOffSell;
    }
}
