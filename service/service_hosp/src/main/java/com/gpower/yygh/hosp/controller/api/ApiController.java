package com.gpower.yygh.hosp.controller.api;

import com.gpower.yygh.hosp.service.ScheduleService;
import com.gpower.yygh.model.hosp.Schedule;
import com.gpower.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;
import com.gpower.yygh.common.exception.YyghException;
import com.gpower.yygh.common.result.Result;
import com.gpower.yygh.common.result.ResultCodeEnum;
import com.gpower.yygh.hosp.common.helper.HttpRequestHelper;
import com.gpower.yygh.hosp.common.utils.MD5;
import com.gpower.yygh.hosp.service.DepartmentService;
import com.gpower.yygh.hosp.service.HospitalService;
import com.gpower.yygh.hosp.service.HospitalSetService;
import com.gpower.yygh.model.hosp.Department;
import com.gpower.yygh.model.hosp.Hospital;
import com.gpower.yygh.vo.hosp.DepartmentQueryVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    //查询排班接口
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取医院系统的签名
        String hospSign = (String) paramMap.get("sign");
        //根据传递过滤医院编号，查询数据库，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        Integer page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : (Integer) paramMap.get("page");
        Integer limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : (Integer) paramMap.get("limit");
        //  签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        //
        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);

        //调用service方法
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    //上传排班接口
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        scheduleService.save(paramMap);
        return Result.ok();
    }


    //删除科室接口
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        String hospSign = (String) paramMap.get("sign");
        //  签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        //
        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    //查询科室接口
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取医院系统的签名
        String hospSign = (String) paramMap.get("sign");
        //根据传递过滤医院编号，查询数据库，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        Integer page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : (Integer) paramMap.get("page");
        Integer limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : (Integer) paramMap.get("limit");
        //  签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        //
        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        //调用service方法
        Page<Department> pageModel = departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    //上传科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取医院系统的签名
        String hospSign = (String) paramMap.get("sign");
        //根据传递过滤医院编号，查询数据库，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //
        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.save(paramMap);
        return Result.ok();


    }

    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取医院系统的签名
        String hospSign = (String) paramMap.get("sign");
        //根据传递过滤医院编号，查询数据库，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //
        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //调用service 方法实现根据医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok();
    }

    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取医院系统的签名
        String hospSign = (String) paramMap.get("sign");
        //根据传递过滤医院编号，查询数据库，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //
        String signKeyMD5 = MD5.encrypt(signKey);
        if (!hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        hospitalService.save(paramMap);
        return Result.ok();
    }
}
