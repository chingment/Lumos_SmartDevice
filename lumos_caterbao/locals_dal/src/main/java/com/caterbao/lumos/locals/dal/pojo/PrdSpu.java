package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class PrdSpu {
    private String id;
    private String merchId;
    private String cumCode;
    private String sysKindIds;
    private String name;
    private String pyIdx;
    private String charTags;
    private Boolean isDelete;
    private String specItems;
    private String displayImgUrls;
    private String briefDes;
    private String detailsDes;
    private String creator;
    private Timestamp createTime;
    private String mender;
    private Timestamp mendTime;
    private String deleter;
    private Timestamp deleteTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getCumCode() {
        return cumCode;
    }

    public void setCumCode(String cumCode) {
        this.cumCode = cumCode;
    }

    public String getSysKindIds() {
        return sysKindIds;
    }

    public void setSysKindIds(String sysKindIds) {
        this.sysKindIds = sysKindIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPyIdx() {
        return pyIdx;
    }

    public void setPyIdx(String pyIdx) {
        this.pyIdx = pyIdx;
    }

    public String getCharTags() {
        return charTags;
    }

    public void setCharTags(String charTags) {
        this.charTags = charTags;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getSpecItems() {
        return specItems;
    }

    public void setSpecItems(String specItems) {
        this.specItems = specItems;
    }

    public String getDisplayImgUrls() {
        return displayImgUrls;
    }

    public void setDisplayImgUrls(String displayImgUrls) {
        this.displayImgUrls = displayImgUrls;
    }

    public String getBriefDes() {
        return briefDes;
    }

    public void setBriefDes(String briefDes) {
        this.briefDes = briefDes;
    }

    public String getDetailsDes() {
        return detailsDes;
    }

    public void setDetailsDes(String detailsDes) {
        this.detailsDes = detailsDes;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender;
    }

    public Timestamp getMendTime() {
        return mendTime;
    }

    public void setMendTime(Timestamp mendTime) {
        this.mendTime = mendTime;
    }

    public String getDeleter() {
        return deleter;
    }

    public void setDeleter(String deleter) {
        this.deleter = deleter;
    }

    public Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }
}
