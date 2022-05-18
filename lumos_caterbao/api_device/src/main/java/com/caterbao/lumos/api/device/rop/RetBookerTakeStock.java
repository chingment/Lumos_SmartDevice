package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.api.device.rop.vo.BookVo;
import com.caterbao.lumos.locals.common.vo.FieldVo;

import java.util.List;

public class RetBookerTakeStock {
    private String flowId;
    private String actionCode;
    private String sheetId;
    private List<BookVo> sheetItems;
    private boolean sheetIsUse;
    private List<BookVo> warnItems;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }


    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }


    public List<BookVo> getSheetItems() {
        return sheetItems;
    }

    public void setSheetItems(List<BookVo> sheetItems) {
        this.sheetItems = sheetItems;
    }

    public List<BookVo> getWarnItems() {
        return warnItems;
    }

    public void setWarnItems(List<BookVo> warnItems) {
        this.warnItems = warnItems;
    }

    public boolean isSheetIsUse() {
        return sheetIsUse;
    }

    public void setSheetIsUse(boolean sheetIsUse) {
        this.sheetIsUse = sheetIsUse;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
}
