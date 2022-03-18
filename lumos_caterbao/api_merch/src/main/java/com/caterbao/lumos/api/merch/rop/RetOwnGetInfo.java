package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.api.merch.rop.vo.MenuVo;

import java.util.List;

public class RetOwnGetInfo {
    private String userName;
    private List<MenuVo> menus;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<MenuVo> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuVo> menus) {
        this.menus = menus;
    }
}
