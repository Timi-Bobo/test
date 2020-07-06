package com.kwb.crm.controller;

import com.kwb.crm.base.BaseController;
import com.kwb.crm.service.UserService;
import com.kwb.crm.utils.LoginUserUtil;
import com.kwb.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }


    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){

        // 从cookie中获取用户Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用Service层，通过主键查询用户对象
        User user = userService.selectByPrimaryKey(userId);
        // 将用户对象设置到请求域中
        request.setAttribute("user",user);

        return "main";
    }
}
