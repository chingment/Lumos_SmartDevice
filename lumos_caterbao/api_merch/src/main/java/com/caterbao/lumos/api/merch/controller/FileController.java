package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.locals.common.CustomResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController extends  BaseController {

    public static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private Environment env;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> upload(MultipartFile file,HttpServletRequest request) throws IOException {
        CustomResult<Object> result = new CustomResult<>();
        try {

            String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
            String fileName = file.getOriginalFilename();  //获取文件名

            String suffixname = fileName.substring(fileName.lastIndexOf("."));//后缀
            String newFileName = UUID.randomUUID() + suffixname;//文件上传后重命名数据库存储

            String folder = "common";

            if(request.getParameter("folder")!=null){
                folder=request.getParameter("folder");
            }

            String save_path = "upload" + File.separator + folder;

            // 图片存储目录及图片名称
            String access_path =env.getProperty("lumos.custom.file-service-url")+File.separator+ save_path + File.separator + newFileName;

            //图片保存路径
            String file_save_dir = staticPath + File.separator + save_path;

            String file_save_path = file_save_dir + File.separator + newFileName;

            File saveFile = new File(file_save_path);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }

            file.transferTo(saveFile);

            Map<String, Object> ret = new HashMap<>();
            ret.put("url",  access_path);
            ret.put("name", newFileName);

            return result.success("上传成功", ret);

        }
        catch (Exception ex){
            logger.error("上传服务发生异常",ex);
            return result.fail("上传服务发生异常");
        }
    }
}
