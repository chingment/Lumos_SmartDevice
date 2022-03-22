package com.caterbao.lumos.locals.common.vo;

import com.caterbao.lumos.locals.common.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.Serializable;
import java.util.List;

public class FileVo implements Serializable {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static String getFileUrl(String json){
        String url="";

        try {

            List<FileVo> obj = JsonUtil.toObject(json, new TypeReference<List<FileVo> >() {
            });

            if(obj!=null&&obj.size()>0) {
                url = obj.get(0).getUrl();
            }
        }
        catch (Exception ex){

        }

        return  url;
    }

    public static String getFileUrl(List<FileVo> vo) {
        String url = "";

        if (vo == null)
            return url;

        if (vo.size() == 0)
            return url;

        return vo.get(0).url;
    }
}
