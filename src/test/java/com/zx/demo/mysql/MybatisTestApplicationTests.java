package com.zx.demo.mysql;

import com.github.jsonzou.jmockdata.JMockData;
import com.zx.demo.dao.UserMapper;
import com.zx.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest // 加载Spring应用上下文
@ActiveProfiles("mysql")
class MybatisTestApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void findByIdTest() {
        Map<String, Object> userMap = userMapper.findById(1);
        assertNotNull(userMap);
        assertEquals(userMap.get("NAME"), "Jone");
        assertEquals(userMap.get("AGE"), 18);
    }

    @Test
    public void getUserByIdTest() {
        User user = userMapper.getUserById(1);
        System.out.println(user);
    }

    @Test
    public void batchInsertUserTest() {
        List<User> userList = new ArrayList<>();
        //mock User对象
        User user = JMockData.mock(User.class);
        userList.add(user);
        //批量插入
        userMapper.batchInsertUser(userList);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void TransactionalTest() {
        List<User> userList = new ArrayList<>();
        User user = new User(6, "cs", 19);
        userList.add(user);
        //批量插入
        userMapper.batchInsertUser(userList);
        // 抛出异常测试回滚
        try {
            int a = 10;
            int b = a / 0;
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void getListUserTest() {
        List<User> listUser = userMapper.getListUser();
        listUser.forEach(System.out::println);
    }
}
