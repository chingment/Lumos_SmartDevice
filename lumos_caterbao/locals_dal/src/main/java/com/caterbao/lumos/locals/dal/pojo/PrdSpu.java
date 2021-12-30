package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class PrdSpu {
    private String id;
    private String merchId;
    private String name;
    private String pyIdx;
    private String charTags;
    private Boolean isDelete;
    private String displayImgUrls;
    private String itemCode;
    private String creator;
    private Timestamp createTime;
    private String mender;
    private Timestamp mendTime;

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

    public String getDisplayImgUrls() {
        return displayImgUrls;
    }

    public void setDisplayImgUrls(String displayImgUrls) {
        this.displayImgUrls = displayImgUrls;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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
}
