package com.caterbao.lumos.locals.biz.model;

import com.caterbao.lumos.locals.common.ImgVo;
import com.caterbao.lumos.locals.common.SpecIdxSkuModel;
import com.caterbao.lumos.locals.common.SpecItemModel;

import java.io.Serializable;
import java.util.List;

public class SpuInfo implements Serializable {
    private String id;
    private String cumCode;
    private String name;
    private List<ImgVo>  displayImgUrls;
    private List<Integer> kindId;
    private String pyIdx;
    private List<String> charTags;
    private String briefDes;
    private List<String> detailsDes;
    private List<SpecItemModel> specItems;
    private List<SpecIdxSkuModel> specIdxSkus;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ImgVo> getDisplayImgUrls() {
        return displayImgUrls;
    }

    public void setDisplayImgUrls(List<ImgVo> displayImgUrls) {
        this.displayImgUrls = displayImgUrls;
    }

    public List<Integer> getKindId() {
        return kindId;
    }

    public void setKindId(List<Integer> kindId) {
        this.kindId = kindId;
    }

    public String getPyIdx() {
        return pyIdx;
    }

    public void setPyIdx(String pyIdx) {
        this.pyIdx = pyIdx;
    }

    public List<String> getCharTags() {
        return charTags;
    }

    public void setCharTags(List<String> charTags) {
        this.charTags = charTags;
    }

    public String getBriefDes() {
        return briefDes;
    }

    public void setBriefDes(String briefDes) {
        this.briefDes = briefDes;
    }

    public List<String> getDetailsDes() {
        return detailsDes;
    }

    public void setDetailsDes(List<String> detailsDes) {
        this.detailsDes = detailsDes;
    }

    public List<SpecItemModel> getSpecItems() {
        return specItems;
    }

    public void setSpecItems(List<SpecItemModel> specItems) {
        this.specItems = specItems;
    }

    public List<SpecIdxSkuModel> getSpecIdxSkus() {
        return specIdxSkus;
    }

    public void setSpecIdxSkus(List<SpecIdxSkuModel> specIdxSkus) {
        this.specIdxSkus = specIdxSkus;
    }
}
