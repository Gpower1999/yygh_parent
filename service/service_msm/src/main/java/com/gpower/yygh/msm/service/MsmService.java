package com.gpower.yygh.msm.service;

import com.gpower.yygh.vo.msm.MsmVo;

public interface MsmService {
    //发送手机验证码
    boolean send(String phone, String code);

    //mq使用的发送短信接口
    boolean send(MsmVo msmVo);
}
