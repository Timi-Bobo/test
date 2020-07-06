package com.kwb.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kwb.crm.Model.UserModel;
import com.kwb.crm.base.BaseService;
import com.kwb.crm.dao.UserMapper;
import com.kwb.crm.query.UserQuery;
import com.kwb.crm.utils.AssertUtil;
import com.kwb.crm.utils.Md5Util;
import com.kwb.crm.utils.UserIDBase64;
import com.kwb.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    /**
     * 多条件分页查询
     *
     * @param userQuery
     * @return
     */
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        Map map = new HashMap();
        //开启分页 传入  显示第几页   一页显示多少条
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        PageInfo<User> pageInfo = new PageInfo(userMapper.queryUserByParms(userQuery));
        map.put("code",200);//设置状态码  随便设
        map.put("success","查询成功");
        map.put("page",pageInfo.getTotal());//当前查询的总数量
        map.put("data",pageInfo.getList());
        return map;
    }


    /**
     * 登录功能实现
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName,String userPwd){
        System.out.println("service层。。。");

        //非空判断
        checkLoginParams(userName,userPwd);
        User user = userMapper.queryUserByName(userName);

        //判断查询对象是否为空，为空抛出异常
        AssertUtil.isTrue(null == user,"用户不存在");
        //判断密码是否正确，正确的话返回
        checkPwd(user.getUserPwd(),userPwd);//传入数据库对象的密码/前台传入的密码
        //返回创建好的消息模型
        return createUserModel(user);
    }

    /**
     * 构建一个返回给前端的模型对象
     *

     * @return
     */
    public UserModel createUserModel(User user){
        System.out.println("构建模型对象");
        UserModel userModel = new UserModel();

        System.out.println("id："+user.getId());

        //用户id进行加密
        String s = UserIDBase64.encoderUserID(user.getId());

        System.out.println("id："+s);

        userModel.setUserIdStr(s);
        userModel.setUserName(user.getUserName());
        userModel.setUserPwd(user.getUserPwd());
        return userModel;
    }

    /**
     * 判断密码是否正确
     *      因为后台代码都进行了加密所以要先把密码进行加密
     *              然后在进行比较，判断密码是否正确
     *  不正确抛出异常
     *
     * @param pwd 数据库对象密码
     * @param userPwd 前台传入密码
     */

    public void checkPwd(String pwd,String userPwd){
        System.out.println("密码判断 & 加密");
        //将前台传入的密码进行加密
        userPwd = Md5Util.encode(userPwd);//加密后的密码

        System.out.println("加密之后的密码："+userPwd);
        System.out.println("数据库查询的代码"+ pwd);


        AssertUtil.isTrue(!pwd.equals(userPwd),"密码不正确");

    }






    /**
     * 非空判断
     * @param userName
     * @param userPwd
     */
    public void checkLoginParams(String userName,String userPwd){
        System.out.println("非空判断");
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空" );
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }

    /**
     * 更新密码
     * 对传入的密码进行非空判断
     *      为空  抛出异常  设置提示信息
     * @param id  用来查询数据库数据  为空说明已经登陆 或者  不存在这个数据
     * @param oldPwd
     * @param newPwd
     * @param configPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)//添加事务
    public void updateUserPassword(Integer id ,
                                        String oldPwd,String newPwd,
                                        String configPwd){


        //非空校验
        checkPasswordParams(id,oldPwd,newPwd,configPwd);
        //创建一个包含想要更新的信息的对象
        User user = new User();
        user.setId(id);
        user.setUserPwd(Md5Util.encode(newPwd));
        user.setUpdateDate(new Date());
        //执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"更新失败");
    }

    /**
     * 对传入的密码进行校验
     * @param id
     * @param oldPwd
     * @param newPwd
     * @param configPwd
     */
    private void checkPasswordParams(Integer id, String oldPwd, String newPwd, String configPwd) {
        System.out.println("非空校验");


        AssertUtil.isTrue(id ==null,"用户未登录");
        //根据id获取对象
        User user = userMapper.selectByPrimaryKey(id);
        //user对象的非空校验  为空说明没有登陆
        AssertUtil.isTrue(user == null,"用户不存在！！！");

        //原始密码的非空校验
        AssertUtil.isTrue(oldPwd==null,"原始密码不能为空！！！");
        //原始密码要与数据库中加密的密码相同
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码输入错误");

        //新密码非空校验
        AssertUtil.isTrue(newPwd==null,"新密码不能为空！！！");
        //新密码与原始不能相同
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码与原始密码不能相同");

        //确认密码的非空校验
        AssertUtil.isTrue(configPwd==null,"确认密码不能为空！！！");
        //新密码与确认密码要保持一直
        AssertUtil.isTrue(!newPwd.equals(configPwd),"新密码与确认密码输入不一致");

    }


}
