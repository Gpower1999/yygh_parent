package com.gpower.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gpower.yygh.common.exception.YyghException;
import com.gpower.yygh.common.helper.JwtHelper;
import com.gpower.yygh.common.result.ResultCodeEnum;
import com.gpower.yygh.model.user.UserInfo;
import com.gpower.yygh.user.mapper.UserInfoMapper;
import com.gpower.yygh.user.service.UserInfoService;
import com.gpower.yygh.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends
        ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //判断是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //TODO 判断手机验证码和输入验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }
        //判断是否第一次登录,根据手机号查询数据库
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        if (userInfo == null) {
            //添加到数据库
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            baseMapper.insert(userInfo);
        }
        //校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }


        //不是第一次，直接登录
        //返回登录信息
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }

        //返回登录用户名
        map.put("name", name);
        //jwt 生成返回token信息
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;


    }
}
