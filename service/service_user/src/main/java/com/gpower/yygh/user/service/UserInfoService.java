package com.gpower.yygh.user.service;

import com.gpower.yygh.vo.user.LoginVo;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface UserInfoService {
    Map<String, Object> loginUser(LoginVo loginVo);
}
