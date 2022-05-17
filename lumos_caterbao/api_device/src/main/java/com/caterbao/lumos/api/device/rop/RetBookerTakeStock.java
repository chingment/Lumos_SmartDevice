package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.api.device.rop.vo.BookVo;

import java.util.List;

public class RetBookerTakeStock {
    private String flowId;
    private String sheetId;
    private List<BookVo> sheetItems;
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
}
