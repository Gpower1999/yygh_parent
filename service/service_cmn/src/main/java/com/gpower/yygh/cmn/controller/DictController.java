package com.gpower.yygh.cmn.controller;

import com.gpower.yygh.cmn.mapper.DictMapper;
import com.gpower.yygh.cmn.service.DictService;
import com.gpower.yygh.common.result.Result;
import com.gpower.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;
    //导入数据字典接口
    @PostMapping("importData")
    public Result importData(MultipartFile file){
        dictService.importDictData(file);
        return Result.ok();
    }


    //导出数据字典接口
    @GetMapping("exportData")
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
    }



    //根据数据id查询子数据列表
    @GetMapping("findChildren/{id}")
    public Result findChildren(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }
}
