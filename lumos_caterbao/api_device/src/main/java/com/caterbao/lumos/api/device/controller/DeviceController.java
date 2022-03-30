package com.caterbao.lumos.api.device.controller;


import com.caterbao.lumos.api.device.rop.RetDeviceCheckAppVersion;
import com.caterbao.lumos.api.device.rop.RetDeviceInitData;
import com.caterbao.lumos.api.device.rop.RopDeviceCheckAppVerion;
import com.caterbao.lumos.api.device.rop.RopDeviceInitData;
import com.caterbao.lumos.api.device.service.DeviceService;
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
@RequestMapping("/device")
public class DeviceController extends BaseController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService){
        this.deviceService=deviceService;
    }

    @Autowired
    private Environment env;

    @RequestMapping(value = "initData", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetDeviceInitData> initData(@RequestBody RopDeviceInitData rop){
        return deviceService.init(this.getCurrentUserId(),this.getCurrentMerchId(),rop);
    }

    @RequestMapping(value = "checkAppVersion", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetDeviceCheckAppVersion> checkAppVersion(@RequestBody RopDeviceCheckAppVerion rop){
        return deviceService.checkAppVerion(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> upload(MultipartFile file, HttpServletRequest request) throws IOException {

        CustomResult<Object> result = new CustomResult<>();
        try {
            String staticPath =env.getProperty("lumos.custom.save-file-path");
            String fileName = file.getOriginalFilename();  //获取文件名

            String suffixname = fileName.substring(fileName.lastIndexOf("."));//后缀

            String newFileName="";
            if(request.getParameter("keepFileName")!=null){
                newFileName=fileName;
            }
            else {
                newFileName = UUID.randomUUID() + suffixname;//文件上传后重命名数据库存储
            }

            String folder = "common";

            if(request.getParameter("folder")!=null){
                folder=request.getParameter("folder");
            }

            String save_path="";
            //判断到是日志转到logs 文件夹下
            if(suffixname.equals(".log")) {
                save_path = "logs" + File.separator + folder;
            }
            else
            {
                save_path = "upload" + File.separator + folder;
            }

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

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> test(){
        return new CustomResult<Object>();
    }

}
