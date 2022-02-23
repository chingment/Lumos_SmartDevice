package com.caterbao.lumos.api.merch.rop;

public class RopAdCreativeAdd {
    private String spaceId;
    private String title;
    private String[] validDate;
    private String fileUrl;


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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
