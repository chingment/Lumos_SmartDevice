package com.caterbao.lumos.locals.biz.model;

import java.io.Serializable;

public class SkuInfo  implements Serializable {
    private String id;
    private String cumCode;
    private String spuId;
    private String name;
    private String barCode;
    private String pyIdx;
    private String imgUrl;
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
}
