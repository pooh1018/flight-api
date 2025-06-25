package com.example.flightapi.security.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.example.flightapi.User.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author
 */
@Getter
@AllArgsConstructor
public class JwtUserDto implements UserDetails {

    @Schema(description = "用户信息", implementation = com.example.flightapi.User.entity.User.class)
    private final User user;

    @Schema(description = "角色权限列表", implementation = AuthorityDto.class)
    private final List<AuthorityDto> authorities;

    public Set<String> getRoles() {
        return authorities.stream().map(AuthorityDto::getAuthority).collect(Collectors.toSet());
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return user.getEmail();
    }

    @JSONField(serialize = false)
    public int getUserId() {
        Object userIdObj = user.getUserId();

        // 处理不同类型的userId
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).intValue();
        } else if (userIdObj instanceof String) {
            return Integer.parseInt((String) userIdObj);
        } else {
            throw new IllegalStateException("JWT中的userId格式无效");
        }
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
