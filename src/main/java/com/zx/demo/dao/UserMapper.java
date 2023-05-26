package com.zx.demo.dao;

import com.zx.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @program: mybatisTest
 * @description:
 * @author: zhou  xun
 * @create: 2023-05-15 12:46
 */
@Mapper
public interface UserMapper {
    /**
     * 根据id查询User
     * @param id 用户id
     * @return java.util.Map
     * @author: zhou  xun
     * @since: 2023-05-26
     */
    @Select("SELECT id, name, age FROM users WHERE id = #{id}")
    Map<String, Object> findById(int id);

    /**
     * 根据id查询User
     * @param id 用户id
     * @return com.zx.demo.model.User
     * @author: zhou  xun
     * @since: 2023-05-26
     */
    @Select("SELECT id, name, age FROM users WHERE id = #{id}")
    User getUserById(int id);

    /**
     *  批量插入UserList
     * @param list
     * @author: zhou  xun
     * @since: 2023-05-26
     */
    @Insert({
            "<script>",
            "insert into users (id, name, age) values ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.id},#{item.name}, #{item.age})",
            "</foreach>",
            "</script>"
    })
    void batchInsertUser(List<User> list);
}