package com.zx.demo;

import com.zx.demo.dao.UserMapper;
import com.zx.demo.model.User;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest // 加载Spring应用上下文
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
    public void queryUserByIdTest() {
        User user = userMapper.queryUserById(1);
        System.out.println(user);
    }
}
