package com.example.flightapi.User.service;

import com.example.flightapi.User.entity.User;
import com.example.flightapi.User.dto.UserRequestDTO;
import com.example.flightapi.common.utils.PageResult;

import java.util.List;
import java.util.Set;

/**
 * @author
 */
public interface UserService {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    User findById(String id);

    /**
     * 新增用户
     * @param resources /
     */
    void create(User resources);

    /**
     * 编辑用户
     * @param resources /
     * @throws Exception /
     */
    void update(User resources) throws Exception;

    /**
     * 删除用户
     * @param ids /
     */
    void delete(Set<String> ids);

    /**
     * 根据用户名查询
     *
     * @param userName /
     * @return /
     */
    User findByName(String userName);

    /**
     * 根据邮箱查询
     *
     * @param email /
     * @return /
     */
    User findByEmail(String email);

    /**
     * 根据用户名查询
     * @param userName /
     * @return /
     */
    User getLoginData(String userName);

    /**
     * 修改密码
     * @param email 邮箱（用户名）
     * @param encryptPassword 密码
     */
    void updatePass(String email, String encryptPassword);

    /**
     * 查询全部
     *
     * @param user 条件 分页参数
     * @return /
     */
    PageResult<User> findAll(UserRequestDTO user);

    /**
     * 查询全部不分页
     *
     * @return /
     */
    List<User> findAll();

    /**
     * 用户自助修改资料
     * @param resources /
     */
    void updateCenter(User resources);

    /**
     * 重置密码
     * @param ids 用户id
     * @param pwd 密码
     */
    void resetPwd(Set<String> ids, String pwd);
}
