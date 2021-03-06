package com.caterbao.lumos.locals.biz.model;

import com.caterbao.lumos.locals.common.vo.FileVo;
import com.caterbao.lumos.locals.common.vo.SpecIdxSkuVo;
import com.caterbao.lumos.locals.common.vo.SpecItemVo;

import java.io.Serializable;
import java.util.List;

public class SpuInfo implements Serializable {
    private String id;
    private String cumCode;
    private String name;
    private List<FileVo>  displayImgUrls;
    private String sysKindIds;
    private String pyIdx;
    private List<String> charTags;
    private String briefDes;
    private List<FileVo> detailsDes;
    private List<SpecItemVo> specItems;
    private List<SpecIdxSkuVo> specIdxSkus;

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

    public List<FileVo> getDisplayImgUrls() {
        return displayImgUrls;
    }

    public void setDisplayImgUrls(List<FileVo> displayImgUrls) {
        this.displayImgUrls = displayImgUrls;
    }

    public String getSysKindIds() {
        return sysKindIds;
    }

    public void setSysKindIds(String sysKindIds) {
        this.sysKindIds = sysKindIds;
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

    public List<FileVo> getDetailsDes() {
        return detailsDes;
    }

    public void setDetailsDes(List<FileVo> detailsDes) {
        this.detailsDes = detailsDes;
    }

    public List<SpecItemVo> getSpecItems() {
        return specItems;
    }

    public void setSpecItems(List<SpecItemVo> specItems) {
        this.specItems = specItems;
    }

    public List<SpecIdxSkuVo> getSpecIdxSkus() {
        return specIdxSkus;
    }

    public void setSpecIdxSkus(List<SpecIdxSkuVo> specIdxSkus) {
        this.specIdxSkus = specIdxSkus;
    }
}
