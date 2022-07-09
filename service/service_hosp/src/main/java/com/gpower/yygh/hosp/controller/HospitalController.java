package com.gpower.yygh.hosp.controller;

import com.gpower.yygh.common.result.Result;
import com.gpower.yygh.hosp.service.HospitalService;
import com.gpower.yygh.model.hosp.Hospital;
import com.gpower.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //医院列表
    @GetMapping("/list/{page}/{limit}")
    public Result listHosp(@PathVariable int page,
                           @PathVariable int limit,
                           HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "更新医院上线状态")
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id, @PathVariable Integer status) {
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    @ApiOperation(value = "医院详情")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id){
        Hospital hospital = hospitalService.getHospByid(id);
        return Result.ok(hospital);
    }
}
