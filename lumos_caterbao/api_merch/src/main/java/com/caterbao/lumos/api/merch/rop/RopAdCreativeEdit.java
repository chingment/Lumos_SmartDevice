package com.caterbao.lumos.api.merch.rop;

public class RopAdCreativeEdit {
    private String id;
    private String title;
    private String[] validDate;
    private String fileUrl;

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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
