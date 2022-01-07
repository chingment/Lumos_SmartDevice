package com.caterbao.lumos.locals.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ImgVo {
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
}
