package com.gpower.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gpower.yygh.model.user.UserInfo;
import com.gpower.yygh.vo.user.LoginVo;
import com.gpower.yygh.vo.user.UserAuthVo;
import com.gpower.yygh.vo.user.UserInfoQueryVo;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface UserInfoService {
    Map<String, Object> loginUser(LoginVo loginVo);

    void userAuth(Long userId, UserAuthVo userAuthVo);

    UserInfo getById(Long userId);

    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    void lock(Long userId, Integer status);

    Map<String, Object> show(Long userId);

    void approval(Long userId, Integer authStatus);
}
