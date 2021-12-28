package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.api.merch.rop.model.MenuModel;

import java.util.List;

public class RetOwnGetInfo {
    private String userName;
    private List<MenuModel> menus;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<MenuModel> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuModel> menus) {
        this.menus = menus;
    }
}
