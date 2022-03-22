package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.api.merch.rop.vo.SkuVo;
import com.caterbao.lumos.api.merch.rop.vo.KindAttrVo;
import com.caterbao.lumos.locals.common.vo.FileVo;
import com.caterbao.lumos.locals.common.vo.SpecItemVo;

import java.util.List;

public class RopProdcutAdd {
    private String name;
    private String cumCode;
    private List<Integer> sysKindIds;
    private List<String> charTags;
    private List<FileVo> detailsDes;
    private List<FileVo> displayImgUrls;
    private List<SpecItemVo> specItems;
    private List<SkuVo> skus;
    private String briefDes;
    private List<KindAttrVo> sysKindAttrs;

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

    public List<Integer> getSysKindIds() {
        return sysKindIds;
    }

    public void setSysKindIds(List<Integer> sysKindIds) {
        this.sysKindIds = sysKindIds;
    }

    public List<String> getCharTags() {
        return charTags;
    }

    public void setCharTags(List<String> charTags) {
        this.charTags = charTags;
    }

    public List<FileVo> getDetailsDes() {
        return detailsDes;
    }

    public void setDetailsDes(List<FileVo> detailsDes) {
        this.detailsDes = detailsDes;
    }

    public List<FileVo> getDisplayImgUrls() {
        return displayImgUrls;
    }

    public void setDisplayImgUrls(List<FileVo> displayImgUrls) {
        this.displayImgUrls = displayImgUrls;
    }

    public List<SpecItemVo> getSpecItems() {
        return specItems;
    }

    public void setSpecItems(List<SpecItemVo> specItems) {
        this.specItems = specItems;
    }

    public List<SkuVo> getSkus() {
        return skus;
    }

    public void setSkus(List<SkuVo> skus) {
        this.skus = skus;
    }

    public String getBriefDes() {
        return briefDes;
    }

    public void setBriefDes(String briefDes) {
        this.briefDes = briefDes;
    }

    public List<KindAttrVo> getSysKindAttrs() {
        return sysKindAttrs;
    }

    public void setSysKindAttrs(List<KindAttrVo> sysKindAttrs) {
        this.sysKindAttrs = sysKindAttrs;
    }
}
