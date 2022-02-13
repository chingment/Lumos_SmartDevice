package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class Order {
    private String id;
    private String merchId;
    private String storeId;
    private String clientId;
    private String icCardId;
    private String skuId;
    private String spuId;
    private String skuName;
    private String skuImg;
    private String skuCumCode;
    private String skuRfId;
    private int quantity;
    private String borrowShopId;
    private String borrowDeviceId;
    private Timestamp borrowTime;

    private String returnShopId;
    private String returnDeviceId;
    private Timestamp returnTime;

    private String creator;
    private Timestamp createTime;
    private String mender;
    private Timestamp mendTime;
}
