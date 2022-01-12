package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.api.merch.rop.model.SkuModel;
import com.caterbao.lumos.api.merch.rop.model.SpecItemModel;
import com.caterbao.lumos.locals.common.ImgVo;

import java.util.List;

public class RopProdcutAdd {
    private String name;
    private String cumCode;
    private List<Integer> kindIds;
    private List<String> charTags;
    private List<ImgVo> detailsDes;
    private List<ImgVo> displayImgUrls;
    private List<SpecItemModel> specItems;
    private List<SkuModel> skus;
    private String briefDes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCumCode() {
        return cumCode;
    }

    public void setCumCode(String cumCode) {
        this.cumCode = cumCode;
    }

    public List<Integer> getKindIds() {
        return kindIds;
    }

    public void setKindIds(List<Integer> kindIds) {
        this.kindIds = kindIds;
    }

    public List<String> getCharTags() {
        return charTags;
    }

    public void setCharTags(List<String> charTags) {
        this.charTags = charTags;
    }

    public List<ImgVo> getDetailsDes() {
        return detailsDes;
    }

    public void setDetailsDes(List<ImgVo> detailsDes) {
        this.detailsDes = detailsDes;
    }

    public List<ImgVo> getDisplayImgUrls() {
        return displayImgUrls;
    }

    public void setDisplayImgUrls(List<ImgVo> displayImgUrls) {
        this.displayImgUrls = displayImgUrls;
    }

    public List<SpecItemModel> getSpecItems() {
        return specItems;
    }

    public void setSpecItems(List<SpecItemModel> specItems) {
        this.specItems = specItems;
    }

    public List<SkuModel> getSkus() {
        return skus;
    }

    public void setSkus(List<SkuModel> skus) {
        this.skus = skus;
    }

    public String getBriefDes() {
        return briefDes;
    }

    public void setBriefDes(String briefDes) {
        this.briefDes = briefDes;
    }
}
