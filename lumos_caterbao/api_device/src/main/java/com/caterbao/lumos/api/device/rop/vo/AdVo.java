package com.caterbao.lumos.api.device.rop.vo;

import java.util.List;

public class AdVo {
    private String spaceId;
    private String name;
    private List<AdCreativeVo> creatives;

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AdCreativeVo> getCreatives() {
        return creatives;
    }

    public void setCreatives(List<AdCreativeVo> creatives) {
        this.creatives = creatives;
    }
}
