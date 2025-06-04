package com.example.flightapi.security.service;

import com.example.flightapi.User.entity.User;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.flightapi.User.service.UserService;
import com.example.flightapi.common.utils.MessageUtils;
import com.example.flightapi.security.dto.JwtUserDto;
import com.example.flightapi.security.dto.AuthorityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 */
@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private MessageUtils messageUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private UserCacheManager userCacheManager;

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        if(jwtUserDto == null){
            User user = userService.getLoginData(username);
            if (user == null) {
                throw new UsernameNotFoundException(messageUtils.getMessage("user.username.not.exist"));
            } else {
                if (!user.getEnabled()) {
                    throw new DisabledException(messageUtils.getMessage("user.disabled"));
                }
                // 初始化JwtUserDto
                List<AuthorityDto> authorities = new ArrayList<>();
                authorities.add(new AuthorityDto());
                jwtUserDto = new JwtUserDto(user, authorities);
                // 添加缓存数据
                userCacheManager.addUserCache(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }

}
