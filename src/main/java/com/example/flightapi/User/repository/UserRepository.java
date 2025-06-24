package com.example.flightapi.User.repository;

import com.example.flightapi.User.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Page<User> findAll(Pageable pageable);

//    Page<User> findAll(String departure, Pageable pageable);

    User findByUsername(String username);

    // 不区分大小写的匹配
//    @Query("{ 'email' : { $regex : ?0, $options : 'i' } }")
    User findByEmail(@Param("email") String email);

    User findByPhone(String phone);

    // 自定义更新密码方法，在MongoDB中需要自己实现更新逻辑
    default void updatePass(String email, String password, java.util.Date lastPasswordResetTime) {
        User user = findByEmail(email);
        if (user != null) {
            user.setPassword(password);
            // 这里可以添加密码重置时间的字段到User类中并更新
            save(user);
        }
    }

    default void resetPwd(Set<String> userIds, String pwd) {
        userIds.forEach(id -> {
            User user = findById(id).orElse(null);
            if (user != null) {
                user.setPassword(pwd);
                save(user);
            }
        });
    }
    
    User findTopByOrderByUserIdDesc();
}
