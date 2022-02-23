package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

public class RopAdCreatives extends BasePageRequest {
    private String spaceId;

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }
}
