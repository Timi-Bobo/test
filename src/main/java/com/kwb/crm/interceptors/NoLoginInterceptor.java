package com.kwb.crm.interceptors;


import com.kwb.crm.exceptions.NoLoginException;
import com.kwb.crm.service.UserService;
import com.kwb.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 *      非法请求拦截  判断用户是否登陆，登陆扽话
 */
public class NoLoginInterceptor implements HandlerInterceptor {


    @Resource
    private UserService userService;
    /**
     * 在请求方法执行之前拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer userid= LoginUserUtil.releaseUserIdFromCookie(request);
        //如果获取的cookie中的id为null  或者  通过id查询到的对象为null
        if (null == userid || null == userService.selectByPrimaryKey(userid)){
             throw new NoLoginException();
        }


        return true;
    }

    /**
     * 请求方法执行之后  视图生成之前执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求执行后  试图生成之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
