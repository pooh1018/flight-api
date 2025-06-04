package com.example.flightapi.User.service.impl;

import com.example.flightapi.security.service.UserCacheManager;
import com.example.flightapi.common.exception.EntityExistException;
import com.example.flightapi.security.service.OnlineUserService;
import com.example.flightapi.User.entity.User;
import com.example.flightapi.User.dto.UserRequestDTO;
import com.example.flightapi.User.repository.UserRepository;
import com.example.flightapi.User.service.UserService;
import com.example.flightapi.common.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisUtils redisUtils;
    private final UserCacheManager userCacheManager;
    private final OnlineUserService onlineUserService;

    public UserServiceImpl(UserRepository userRepository, RedisUtils redisUtils, UserCacheManager userCacheManager, OnlineUserService onlineUserService) {
        this.userRepository = userRepository;
        this.redisUtils = redisUtils;
        this.userCacheManager = userCacheManager;
        this.onlineUserService = onlineUserService;
    }

    @Override
    public PageResult<User> findAll(UserRequestDTO user) {
        // MongoDB分页查询
        Sort sort = Sort.by(Sort.Direction.fromString(user.getSortDirection()), user.getSortField());
        Pageable pageable = PageRequest.of(user.getPageNo() - 1, user.getPageSize(), sort);
        Page<User> pageResult = userRepository.findAll(pageable);
        return new PageResult<>(
                pageResult.getContent(),
                pageResult.getTotalElements()
        );
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        String key = CacheKey.USER_ID + id;
        User user = redisUtils.get(key, User.class);
        if (user == null) {
            user = userRepository.findById(id).orElse(null);
            if (user != null) {
                redisUtils.set(key, user, 1, TimeUnit.DAYS);
            }
        }
        return user;
    }

    @Override
    public User findByEmail(String email)  {
        User user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public void create(User resources) {
        if (userRepository.findByEmail(resources.getEmail()) != null) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        if (userRepository.findByPhone(resources.getPhone()) != null) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        resources.setEnabled(true);
        userRepository.save(resources);
    }

    @Override
    public void update(User resources) throws Exception {
        User user = userRepository.findById(resources.getId()).orElse(null);
        if (user == null) {
            throw new EntityExistException(User.class, "id", resources.getId());
        }
        User user1 = userRepository.findByEmail(resources.getEmail());
        User user2 = userRepository.findByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }

        // 如果用户被禁用，则清除用户登录信息
        if (!resources.getEnabled()) {
            onlineUserService.kickOutForUsername(resources.getEmail());
        }

        user.setUserId(resources.getUserId());
        user.setNickName(resources.getNickName());
        user.setCountry(resources.getCountry());
        user.setEmail(resources.getEmail());
        user.setFirstName(resources.getFirstName());
        user.setLastName(resources.getLastName());
        user.setPhone(resources.getPhone());
        user.setEnabled(resources.getEnabled());
        userRepository.save(user);
        // 清除缓存
        delCaches(user.getId(), user.getEmail());
    }

    @Override
    public void updateCenter(User resources) {
        User user = userRepository.findById(resources.getId()).orElse(null);
        if (user == null) {
            throw new EntityExistException(User.class, "id", resources.getId());
        }
        User user1 = userRepository.findByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        userRepository.save(user);
        // 清理缓存
        delCaches(user.getId(), user.getEmail());
    }

    @Override
    public void delete(Set<String> ids) {
        ids.forEach(id -> {
            // 清理缓存
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                delCaches(user.getId(), user.getEmail());
            }
        });
        userRepository.deleteAllById(ids);
    }

    @Override
    public User findByName(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public User getLoginData(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updatePass(String email, String pass) {
        userRepository.updatePass(email, pass, new Date());
        flushCache(email);
    }

    @Override
    public void resetPwd(Set<String> ids, String pwd) {
        userRepository.resetPwd(ids, pwd);
        ids.forEach(id -> {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                // 清除缓存
                flushCache(user.getEmail());
                // 强制退出
                onlineUserService.kickOutForUsername(user.getEmail());
            }
        });
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(String id, String username) {
        redisUtils.del(CacheKey.USER_ID + id);
        flushCache(username);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        userCacheManager.cleanUserCache(username);
    }
}
