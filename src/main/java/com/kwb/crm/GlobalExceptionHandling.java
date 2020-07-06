package com.kwb.crm;

import com.alibaba.fastjson.JSON;
import com.kwb.crm.base.ResultInfo;
import com.kwb.crm.exceptions.NoLoginException;
import com.kwb.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;

/**
 * 全局异常处理类
 *  只能处理返回json格式的异常
 *      返回格式有两种     有些异常返回字符串，有些返回试图，页面前端编写
 *          json  m没有声明@ResponseBody注解  返回试图
 *          试图

 */
@Component
@ControllerAdvice//表示是一个异常处理类
public class GlobalExceptionHandling implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        if (e instanceof NoLoginException){
            //拦截页面到页面到登陆页面
            //请求转发
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;


        }


        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("code",300);
        mv.addObject("msg","系统处理异常，请稍等。。。");

        //判断是否是HandlerMethod（方法）的实例 其实就是判断是不是我们controller的方法
        //判断handler是否是HandlerMethod（方法）的实例
        if (handler instanceof HandlerMethod){
            //将handler强转为handlerMethod
            HandlerMethod handlerMethod = (HandlerMethod)handler;

            //获取到方法上的ResponseBody注解

            /**
             * handlerMethod.getMethod()
             *      获取的就是controller层的那些方法
             */
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            /**
             * 判断是返回视图
             *      还是json格式数据
             */
            if (responseBody==null){
                //返回视图

                if (e instanceof ParamsException){
                    //判断是不是自定义异常是的话   设置状态码和提示信息到ModelAndView
                    ParamsException p = (ParamsException)e;
                    mv.addObject("code",p.getCode());
                    mv.addObject("msg",p.getMsg());
                }
                return mv;

            }else {
                //返回json数据  系统异常
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，请重试");
                //如果是自定义异常  设置一下  然后通过流响应出去
                if (e instanceof ParamsException){
                    ParamsException p = (ParamsException)e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                //设置相应类型和编码格式
                response.setContentType("application/json;charset = utf-8");
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }finally {
                    if (out != null ){
                        out.close();
                    }
                }
                return null;


            }

        }

        return mv;


    }
}
