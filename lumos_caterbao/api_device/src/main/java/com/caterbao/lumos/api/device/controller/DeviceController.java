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
            String fileName = file.getOriginalFilename();  //???????????????

            String suffixname = fileName.substring(fileName.lastIndexOf("."));//??????

            String newFileName="";
            if(request.getParameter("keepFileName")!=null){
                newFileName=fileName;
            }
            else {
                newFileName = UUID.randomUUID() + suffixname;//???????????????????????????????????????
            }

            String folder = "common";

            if(request.getParameter("folder")!=null){
                folder=request.getParameter("folder");
            }

            String save_path="";
            //????????????????????????logs ????????????
            if(suffixname.equals(".log")) {
                save_path = "logs" + File.separator + folder;
            }
            else
            {
                save_path = "upload" + File.separator + folder;
            }

            // ?????????????????????????????????
            String access_path =env.getProperty("lumos.custom.file-service-url")+File.separator+ save_path + File.separator + newFileName;

            //??????????????????
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

            return result.success("????????????", ret);

        }
        catch (Exception ex){
            logger.error("????????????????????????",ex);
            return result.fail("????????????????????????");
        }

    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> test(){
        return new CustomResult<Object>();
    }

}
