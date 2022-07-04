package com.gpower.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gpower.yygh.common.result.Result;
import com.gpower.yygh.hosp.common.utils.MD5;
import com.gpower.yygh.hosp.service.HospitalSetService;
import com.gpower.yygh.model.hosp.HospitalSet;
import com.gpower.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    // 1.查询医院设置表所有信息
    @ApiOperation(value = "查询医院设置表所有信息")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    // 2 逻辑删除医院设置
    @ApiOperation(value = "删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHosp(@PathVariable Long id) {
        boolean b = hospitalSetService.removeById(id);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 3条件查询带分页
    @PostMapping("findPage/{current/{limit}")
    public Result findPageHospSet(@PathVariable long current
                                 ,@PathVariable long limit
                                 ,@RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 创建page对象,传入当前页每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        //构建条件
        QueryWrapper<HospitalSet> warpper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)){
            warpper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(hoscode)){
            warpper.like("hoscode", hoscode);
        }
        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, warpper);
        return Result.ok(pageHospitalSet);
    }
    // 4添加医院设置
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        //设置状态1使用，0不能使用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        boolean save = hospitalSetService.save(hospitalSet);
        if (save){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }
    // 5根据id获取医院设置
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }
    // 6修改医院设置
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean b = hospitalSetService.updateById(hospitalSet);
        if (b){
            return Result.ok();
        }else{
            return Result.fail();
        }

    }
    // 7批量删除医院设置
    @DeleteMapping("bacthRomove")
    public Result bacthRomove(@RequestBody List<Long> idList){
        boolean b = hospitalSetService.removeByIds(idList);
        if (b){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }
    //8医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,@PathVariable Integer status){
        // 根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        boolean b = hospitalSetService.updateById(hospitalSet);
        if (b){
            return Result.ok();
        }else{
            return Result.fail();
        }

    }
    //9发送签名的密钥
    @PutMapping("sendSignKey/{id}/{status}")
    public Result sendSignKey(@PathVariable Long id){
        // 根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO发送短信
        return Result.ok();
    }
}
