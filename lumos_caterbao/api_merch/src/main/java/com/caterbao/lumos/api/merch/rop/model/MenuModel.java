package com.caterbao.lumos.api.merch.rop.model;

public class MenuModel {
    private String id;
    private String code;
    private String pId;
    private String title;
    private String component;
    private String path;
    private String icon;
    private Boolean isSideBar;
    private Boolean isNavBar;
    private Boolean isRouter;
    private String redirect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getRouter() {
        return isRouter;
    }

    public void setRouter(Boolean router) {
        isRouter = router;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getIsSideBar() {
        return isSideBar;
    }

    public void setIsSideBar(Boolean isSideBar) {
        this.isSideBar = isSideBar;
    }

    public Boolean getIsNavBar() {
        return isNavBar;
    }

    public void setIsNavBar(Boolean isNavBar) {
        this.isNavBar = isNavBar;
    }

    public Boolean getIsRouter() {
        return isRouter;
    }

    public void setIsRouter(Boolean isRouter) {
        this.isRouter = isRouter;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
