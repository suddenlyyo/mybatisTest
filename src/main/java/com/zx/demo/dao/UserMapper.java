package com.zx.demo.dao;

import com.zx.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @program: mybatisTest
 * @description:
 * @author: zhou  xun
 * @create: 2023-05-15 12:46
 */
@Mapper
public interface UserMapper {
    @Select("SELECT id, name, age FROM users WHERE id = #{id}")
    Map<String,Object> findById(int id);
    @Select("SELECT id, name, age FROM users WHERE id = #{id}")
    User queryUserById(int id);
}