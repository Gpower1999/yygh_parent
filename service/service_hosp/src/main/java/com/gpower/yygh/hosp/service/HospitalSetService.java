package com.gpower.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gpower.yygh.model.hosp.HospitalSet;
import com.gpower.yygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}
