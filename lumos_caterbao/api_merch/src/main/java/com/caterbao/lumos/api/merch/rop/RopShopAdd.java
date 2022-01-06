package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.ImgVo;

import java.util.List;

public class RopShopAdd {
    private String name;
    private List<ImgVo> displayImgUrls;
    private String contactName;
    private String contactPhone;
    private String contactAddress;

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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }
}