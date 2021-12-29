package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class SysMenu {
    private String id;
    private String code;
    private String pId;
    private String title;
    private String component;
    private String path;
    private String icon;
    private Integer priority;
    private Integer depth;
    private Boolean isSideBar;
    private Boolean isNavBar;
    private Boolean isRouter;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Boolean getSideBar() {
        return isSideBar;
    }

    public void setSideBar(Boolean sideBar) {
        isSideBar = sideBar;
    }

    public Boolean getNavBar() {
        return isNavBar;
    }

    public void setNavBar(Boolean navBar) {
        isNavBar = navBar;
    }

    public Boolean getRouter() {
        return isRouter;
    }

    public void setRouter(Boolean router) {
        isRouter = router;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
