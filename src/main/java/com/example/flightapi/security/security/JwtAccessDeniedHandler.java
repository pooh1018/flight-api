package com.example.flightapi.security.security;

import com.example.flightapi.common.exception.handler.ApiResult;
import com.example.flightapi.common.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author /
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   @Autowired
   private MessageUtils messageUtils;

   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
      //当用户在没有授权的情况下访问受保护的REST资源时，将调用此方法发送403 Forbidden响应
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonResponse = objectMapper.writeValueAsString(ApiResult.fail(String.valueOf(HttpStatus.FORBIDDEN.value()), messageUtils.getMessage("common.forbidden")));
      response.getWriter().write(jsonResponse);
   }
}
