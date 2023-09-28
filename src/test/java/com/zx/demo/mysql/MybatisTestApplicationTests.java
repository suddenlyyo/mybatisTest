package com.zx.demo.mysql;

import com.github.jsonzou.jmockdata.JMockData;
import com.zx.demo.dao.UserMapper;
import com.zx.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        if (!ObjectUtils.isEmpty(user)) {
            System.out.println(user);
        }
    }

    @Test
    public void batchInsertUserTest() {
        List<User> userList = userMapper.getListUser();
        List<Integer> ids = userList.stream().map(User::getId).collect(Collectors.toList());
        //mock 10000个数据
        IntStream.rangeClosed(1, 10000).forEach(i -> {
            //mock User对象
            User user = JMockData.mock(User.class);
            if (!ids.contains(user.getId())) {
                userList.add(user);
            }
        });

        //批量插入
        userMapper.batchInsertUser(userList);
    }

    /**
     * <p>
     * 七种传播特性:<br>
     * REQUIRED：<em>默认传播特性</em>。如果当前存在事务，则加入该事务，否则创建一个新事务。该传播特性表示当前的操作必须在一个事务中运行，如果没有事务则新建一个事务。<br>
     * SUPPORTS：如果当前存在事务，则加入该事务，否则不使用事务。该传播特性表示当前的操作如果在一个事务中，则在事务中执行，否则以非事务方式执行。<br>
     * MANDATORY：必须在一个事务中执行，否则抛出异常。该传播特性表示当前的操作必须在一个事务中运行，否则抛出异常。<br>
     * REQUIRES_NEW：如果当前存在事务，则挂起该事务，并创建一个新事务；否则创建一个新事务。该传播特性表示当前的操作必须在一个新的事务中运行，如果当前已经存在事务，则将当前事务挂起，并新建一个事务。<br>
     * NOT_SUPPORTED：不应该在一个事务中运行。该传播特性表示当前的操作必须以非事务方式运行，如果在一个事务中则将该事务挂起。<br>
     * NEVER：不应该在一个事务中运行，否则抛出异常。该传播特性表示当前的操作必须以非事务方式运行，否则抛出异常。<br>
     * NESTED：如果当前存在事务，则在该事务的嵌套事务中执行；否则创建一个新事务。该传播特性表示当前的操作必须在一个嵌套事务中运行，如果当前已经存在事务，则在该事务的嵌套事务中执行，否则新建一个事务。<br>
     * </p>
     * <hr>
     * <p>
     * 隔离级别:<br>
     * （1）DEFAULT<br>
     * 　　使用数据库设置的隔离级别<em>默认级别</em>，由DBA 默认的设置来决定隔离级别。<br>
     * （2）READ_UNCOMMITTED<br>
     * 　　这是事务最低的隔离级别，它充许别外一个事务可以看到这个事务未提交的数据。<br>
     * 　　会出现脏读、不可重复读、幻读 （隔离级别最低，并发性能高）。<br>
     * （3）READ_COMMITTED<br>
     * 　　保证一个事务修改的数据提交后才能被另外一个事务读取。另外一个事务不能读取该事务未提交的数据。<br>
     * 　　可以避免脏读，但会出现不可重复读、幻读问题（锁定正在读取的行）。<br>
     * （4）REPEATABLE_READ(innodb默认的数据库隔离级别)<br>
     * 　　可以防止脏读、不可重复读，但会出幻读（锁定所读取的所有行）。<br>
     * （5）SERIALIZABLE<br>
     * 　　这是花费最高代价但是最可靠的事务隔离级别，事务被处理为顺序执行。<br>
     * 　　保证所有的情况不会发生（锁表）。<br>
     * <hr>
     * 注意：myisam与innodb的区别！<br>
     * myisam只有表级锁，innodb可以支持表级锁和行级锁（默认行级锁）。<br>
     * myisam强调查询性能，查询操作比innodb快，但是不提供事务支持。innodb提供事务、外键等高级功能，同时具有事务、回滚、崩溃修复能力。<br>
     * myisam不支持外键，innodb支持外键。<br>
     * MVCC支持：仅有innodb支持MVCC，同时MVCC只在READ_COMMITTED和REPEATABLE_READ这两个隔离级别下工作。MVCC：多版本并发控制，<br>
     * 使不同事务的读写操作并发执行，提升系统性能。</p>
     *
     * @author: zhou  xun
     * @since: 2023-08-08
     */
    @Test
    //@Rollback(false)//springboot Junit单元测试默认事务不提交,要提交要设置这个注解,但是开启这个注解后跑异常不回滚了
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    public void transactionalTest() {
        List<User> userList = new ArrayList<>();
        //mock User对象
        User user = JMockData.mock(User.class);
        userList.add(user);
        //批量插入
        userMapper.batchInsertUser(userList);
        // 抛出异常测试回滚
        int a = 10;
        int b = a / 0;
    }

    @Test
    public void getListUserTest() {
        List<User> listUser = userMapper.getListUser();
        if (!listUser.isEmpty()) {
            System.out.println("以下是查询到的用户信息:");
            listUser.forEach(System.out::println);
        }
    }

    @Test
    public void getListUserInTest() {
        List<User> listUser = userMapper.getListUser();
        List<User> queryIn = new ArrayList<>();
        if (!listUser.isEmpty()) {
            List<Integer> ids = listUser.stream().map(User::getId).collect(Collectors.toList());
            Consumer<List<Integer>> queryInConsumer = (list) -> queryIn.addAll(userMapper.getListUserById(list));
            pageSubList(1000, 500, ids, queryInConsumer);
            queryIn.forEach(System.out::println);
        }
    }

    /**
     * 分页处理，解决sql查询in参数大于1000报错问题
     *
     * @param sizeLimit 单次查询的in参数上限
     * @param pageSize  需要分页处理查询的数据
     * @param data      需要分页处理查询的数据
     * @param queryIn   查询的消费着
     * @return
     * @author: zhou  xun
     * @since: 2023-09-22
     */
    private <T> void pageSubList(Integer sizeLimit, Integer pageSize, List<T> data, Consumer<List<T>> queryIn) {
        if (data == null) {
            return;
        }
        //in查询大于1000 数据库会报错，
        if(sizeLimit>1000){
            return;
        }
        if (data.size() > sizeLimit) {
            int totalPage = (data.size() + pageSize - 1) / pageSize;
            for (int paNow = 0; paNow < totalPage; paNow++) {
                int endIndex = Math.min(data.size(), pageSize);
                List<T> subIds = data.subList(0, endIndex);
                queryIn.accept(subIds);
                //移除已经截取过的数据
                data.removeIf(subIds::contains);
            }
        } else {
            queryIn.accept(data);
        }
    }

}
