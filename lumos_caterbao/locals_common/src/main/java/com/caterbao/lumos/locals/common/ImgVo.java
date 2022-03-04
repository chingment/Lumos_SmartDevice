package com.caterbao.lumos.locals.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;

public class ImgVo implements Serializable {
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

    public static String getMainImgUrl(String json){
        String url="";

        try {


            ObjectMapper objectMapper = new ObjectMapper();
            List<ImgVo> obj = objectMapper.readValue(json,
                    new TypeReference<List<ImgVo> >() {
                    });

            if(obj!=null&&obj.size()>0){
                url=obj.get(0).getUrl();
            }
        }
        catch (Exception ex){

        }

        return  url;
    }

    public static String getMainImgUrl(List<ImgVo> vo) {
        String url = "";

        if (vo == null)
            return url;

        if (vo.size() == 0)
            return url;

        return vo.get(0).url;
    }
}
