package com.jin.agentx_learning.infrastructure.interceptor;


import com.jin.agentx_learning.domain.user.model.UserEntity;
//import com.jin.agentx_learning.domain.user.service.UserDomainService;
import com.jin.agentx_learning.infrastructure.auth.UserContext;
import com.jin.agentx_learning.infrastructure.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/** 管理员权限拦截器 用于拦截需要管理员权限的请求 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

//    private final UserDomainService userDomainService;
//
//    public AdminAuthInterceptor(UserDomainService userDomainService) {
//        this.userDomainService = userDomainService;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String userId = UserContext.getCurrentUserId();
//        UserEntity user = userDomainService.getUserInfo(userId);
//
//        if (user == null || !user.isAdmin()) {
//            throw new BusinessException("无权限访问管理功能");
//        }
//
//        return true;
//    }
}