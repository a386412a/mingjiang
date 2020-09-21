package com.test.springdata.repsotory;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.test.springdata.entity.Persion;

/**
 *
 */
//public interface PersionRepsotory extends Repository<Persion,Integer> {
public interface PersionRepsotory extends JpaRepository<Persion,Integer>,JpaSpecificationExecutor<Persion> {
    Persion getByLastName(String lastName);
    // 关键字测试一：where lastName like ?% and id < ?
    List<Persion> getByLastNameStartingWithAndIdLessThan(String lastName, Integer id);
    // 关键字测试二：where lastName like ?% and id < ?
    List<Persion> getByLastNameEndingWithAndId(String lastName, Integer id);
    // wgere email in(?,?,?) or brith < ?
    List<Persion> getByEmailInOrBrithLessThan(List<String> emails, Date brith);

    // 自定义查询：查询id值最大的Persion
    // Query自定义的JPQL语句中的Persion不是表明,而是实体类的名字
    @Query("select p from Persion p where p.id=(select max(p2.id) from Persion p2)")
    Persion getPersionByMaxId();

    // JPQL传参的第一种方式(占位符)：
    @Query("select p from Persion p where p.id=?1 and p.lastName=?2")
    Persion getByIdAndName(Integer id,String lastName); // 参数需要与占位符一一对应

    // JPQL传参的第二种方式(命名参数)：参数顺序无需对应
    @Query("select p from Persion p where p.email= :email and p.lastName= :lastName")
    Persion getByEmailAndName(@Param("lastName") String lastName, @Param("email") String email);

    // JPQL传参的第三种方式(占位符like查询)：
    // 如果参数中带了%,则HPQL语句中可以省略
    @Query("select p from Persion p where p.email like %?1% and p.lastName like %?2%")
    Persion getLike1(String email, String lastName);

    // JPQL传参的第四种方式(命名参数like查询)：
    // 如果参数中带了%,则HPQL语句中可以省略
    @Query("select p from Persion p where p.email like %:email% and p.lastName like %:lastName%")
    Persion getLike2(@Param("email") String email, @Param("lastName") String lastName);

    // 原生的sql查询,nativeQuery = true即设置原生sql查询
    @Query(value = "select count(id) from jpa_persion",nativeQuery = true)
    Integer counts();

    // 1.更新数据,JPQL不支持insert语句,且更 新语、删除 句时必须使用 @Modifying注解进行修饰
    // 2.@modifying用于通知springdata这是一个更新或删除语句
    // 3.更新和删除语句需要在Service中添加事务操作
    // 4.springdata的每个方法都默认添加了一个只读事务,不能完成修改和更新
    @Modifying
    @Query("update Persion p set p.email = :email where p.id = :id")
    Integer update(@Param("email") String email,@Param("id") Integer id);
}

