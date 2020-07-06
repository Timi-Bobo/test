package com.kwb.crm.dao;

import com.kwb.crm.base.BaseMapper;
import com.kwb.crm.query.UserQuery;
import com.kwb.crm.vo.User;

import java.util.List;

/**
 * 继承bean中接口的方法，这样也就能调用之前的方法了
 *
 */

public interface UserMapper  extends BaseMapper<User,Integer> {
    //根据用户名查询对象
    public User queryUserByName(String userNane);
    //传入邮箱名字
    List<User> queryUserByParms(UserQuery userQuery);

}