package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.vo.FileVo;

import java.util.List;

public class RopAdCreativeEdit {
    private String id;
    private String title;
    private String[] validDate;
    private List<FileVo> fileUrl;
    private boolean isDisable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<FileVo> getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(List<FileVo> fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(boolean isDisable) {
        this.isDisable = isDisable;
    }
}
