package com.gpower.yygh.msm.controller;

import com.gpower.yygh.common.result.Result;
import com.gpower.yygh.msm.service.MsmService;
import com.gpower.yygh.msm.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
@RestController
@RequestMapping("/api/msm")
public class MsmApiController {
    @Autowired
    private MsmService msmService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //发送手机验证码
    @ApiOperation(value = "发送手机验证码")
    @GetMapping("send/{phone}")
    public Result sendCode(@PathVariable String phone){
        //从redis里面获取验证码，如果可以获取到，返回ok
        // key 手机号 value 验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return Result.ok();
        }
        //如果从redis获取不到，生成验证码，整合短信服务进行发送
        code = RandomUtil.getSixBitRandom();
        boolean isSend = msmService.send(phone,code);
        if (isSend){
            //生成验证码放入redis，设置有效时间
            redisTemplate.opsForValue().set(phone,code,2, TimeUnit.MINUTES);
            return Result.ok();
        }else {
            return Result.fail().message("发送短信失败");
        }
    }
}
