package com.soft.controller;

import com.soft.utils.AliyunOssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileController {

    //注入文件上传工具类
    @Autowired
    private AliyunOssUtils aliyunOssUtils;

    /*处理文件上传请求*/
    @RequestMapping("/upload")
    public String fileUpload(MultipartFile mf){

        try{
            //获得原始的文件名
            String oldName = mf.getOriginalFilename();
            String ext= oldName.substring(oldName.lastIndexOf("."));
            String name= UUID.randomUUID().toString()+ext;
            byte[] bytes = mf.getBytes();
            String path = aliyunOssUtils.uploadFile(name, bytes);
            return path;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "ok";
    }


}


