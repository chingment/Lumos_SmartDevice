package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.ImgVo;

import java.util.List;

public class RopAdCreativeAdd {
    private String spaceId;
    private String title;
    private String[] validDate;
    private List<ImgVo> fileUrl;


    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getValidDate() {
        return validDate;
    }

    public void setValidDate(String[] validDate) {
        this.validDate = validDate;
    }

    public List<ImgVo> getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(List<ImgVo> fileUrl) {
        this.fileUrl = fileUrl;
    }
}
