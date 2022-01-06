package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController extends  BaseController {

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult upload(MultipartFile file) throws IOException {

        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());

        //String holder = "G:\\0001-myproject\\mysecurity\\mysecurity-demo\\src\\main\\java\\com\\nxz\\controller";

        //ile localFile = new File(holder, new Date().getTime() + ".txt");

        //file.transferTo(localFile);


        return CustomResult.success("保存成功");
    }
}
