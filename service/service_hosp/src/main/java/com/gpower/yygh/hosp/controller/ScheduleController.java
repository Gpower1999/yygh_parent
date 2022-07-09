package com.gpower.yygh.hosp.controller;

import com.gpower.yygh.common.result.Result;
import com.gpower.yygh.hosp.service.ScheduleService;
import com.gpower.yygh.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
@CrossOrigin
public class ScheduleController {
    @Autowired
    private ScheduleService  scheduleService;
    //根据医院编号 和 科室编号，查询排班规则
    @ApiOperation(value = "查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRuel(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){
        Map<String,Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);

    }
    //根据医院编号、科室编号、工作日期查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleDatail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDatail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list = scheduleService.getScheduleDatail(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}
