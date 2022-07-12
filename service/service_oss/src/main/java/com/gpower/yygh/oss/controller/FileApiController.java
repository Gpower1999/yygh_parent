package com.gpower.yygh.oss.controller;

import com.gpower.yygh.common.result.Result;
import com.gpower.yygh.oss.servicee.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {
    //上次文件到阿里云oss
    @Autowired
    private FileService fileService;
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){
        //获取上传文件
        String path = fileService.upload(file);
        return Result.ok(path);
    }
}
