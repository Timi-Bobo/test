package com.kwb.crm.controller;

import com.kwb.crm.Model.UserModel;
import com.kwb.crm.base.BaseController;
import com.kwb.crm.base.ResultInfo;
import com.kwb.crm.service.UserService;
import com.kwb.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 进入用户页面
     * @return
     */
    public String index(){
        return "";

    }



    /**
     * 登录功能
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("user/login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        System.out.println(userName+"-"+userPwd);

        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.userLogin(userName, userPwd);

        System.out.println("加密后的id："+userModel.getUserIdStr());
        //将返回的用户模型对象设置
        resultInfo.setResult(userModel);

        return resultInfo;
    }


    /**
     * 进入修改密码页面
     * @return
     */
    @RequestMapping("user/toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }







    /**
     * 接收前台传参  然后进行判断
     * 更新密码
     *  前段传入I密码之后的操作
     *  调用service层
     * @param
     * @param configPwd
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @PostMapping("user/updatePassword")
    @ResponseBody
    public ResultInfo updatePassWord(HttpServletRequest request ,
                                     String oldPwd, String newPwd,
                                     String configPwd){


        ResultInfo resultInfo = new ResultInfo();
        //获取cookie中的id
        Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updateUserPassword(id,oldPwd,newPwd,configPwd);
        return resultInfo;

    }



}
