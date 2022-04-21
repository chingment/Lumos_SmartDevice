package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.api.device.rop.vo.RfTagVo;

import java.util.HashMap;
import java.util.List;

public class RopBookerTakeStock {
    private String deviceId;
    private String msgId;
    private String msgMode;
    private String flowId;
    private String actionCode;
    private String actionData;
    private String actionTime;
    private String actionRemark;

    private List<RfTagVo> rfTags;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<RfTagVo> getRfTags() {
        return rfTags;
    }

    public void setRfTags(List<RfTagVo> rfTags) {
        this.rfTags = rfTags;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgMode() {
        return msgMode;
    }

    public void setMsgMode(String msgMode) {
        this.msgMode = msgMode;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public String getActionRemark() {
        return actionRemark;
    }

    public void setActionRemark(String actionRemark) {
        this.actionRemark = actionRemark;
    }
}
