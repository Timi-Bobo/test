package com.kwb.crm;

import static org.junit.Assert.assertTrue;

import com.kwb.crm.dao.UserMapper;
import com.kwb.crm.vo.User;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Resource
    private UserMapper userMapper;

    @Test
    public void shouldAnswerWithTrue() {

        User admin = userMapper.queryUserByName("admin");
        System.out.println(admin);
    }
}
