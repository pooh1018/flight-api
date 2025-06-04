package com.example.flightapi.security.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.flightapi.security.config.SecurityProperties;
import com.example.flightapi.security.security.TokenProvider;
import com.example.flightapi.security.dto.JwtUserDto;
import com.example.flightapi.security.dto.OnlineUserDto;
import com.example.flightapi.common.utils.*;
import com.example.flightapi.common.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */
@Service
@Slf4j
@AllArgsConstructor
public class OnlineUserService {

    private final SecurityProperties properties;
    private final TokenProvider tokenProvider;
    private final RedisUtils redisUtils;

    /**
     * 保存在线用户信息
     * @param jwtUserDto /
     * @param token /
     * @param request /
     */
    public void save(JwtUserDto jwtUserDto, String token, HttpServletRequest request){
        String ip = StringUtils.getIp(request);
        String id = tokenProvider.getId(token);
        String browser = StringUtils.getBrowser(request);
        String address = StringUtils.getCityInfo(ip);
        OnlineUserDto onlineUserDto = null;
        try {
            onlineUserDto = new OnlineUserDto(
                    id,
                    jwtUserDto.getUsername(),
                    jwtUserDto.getUser().getNickName(),
                    browser,
                    ip,
                    address,
                    EncryptUtils.desEncrypt(token),
                    new Date());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        String loginKey = tokenProvider.loginKey(token);
        redisUtils.set(loginKey, onlineUserDto, properties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);
    }

    /**
     * 查询全部数据
     * @param username /
     * @param pageable /
     * @return /
     */
    public PageResult<OnlineUserDto> getAll(String username, Pageable pageable){
        List<OnlineUserDto> onlineUserDtos = getAll(username);
        return PageUtil.toPage(
                PageUtil.paging(pageable.getPageNumber(), pageable.getPageSize(), onlineUserDtos),
                onlineUserDtos.size()
        );
    }

    /**
     * 查询全部数据，不分页
     * @param username /
     * @return /
     */
    public List<OnlineUserDto> getAll(String username){
        String loginKey = properties.getOnlineKey() +
                (StringUtils.isBlank(username) ? "" : "*" + username);
        List<String> keys = redisUtils.scan(loginKey + "*");
        Collections.reverse(keys);
        List<OnlineUserDto> onlineUserDtos = new ArrayList<>();
        for (String key : keys) {
            onlineUserDtos.add(redisUtils.get(key, OnlineUserDto.class));
        }
        onlineUserDtos.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserDtos;
    }

    /**
     * 退出登录
     * @param token /
     */
    public void logout(String token) {
        String loginKey = tokenProvider.loginKey(token);
        redisUtils.del(loginKey);
    }

    /**
     * 查询用户
     * @param key /
     * @return /
     */
    public OnlineUserDto getOne(String key) {
        return redisUtils.get(key, OnlineUserDto.class);
    }

    /**
     * 根据用户名强退用户
     * @param email /
     */
    public void kickOutForUsername(String email) {
        String loginKey = properties.getOnlineKey() + email + "*";
        redisUtils.scanDel(loginKey);
    }
}
