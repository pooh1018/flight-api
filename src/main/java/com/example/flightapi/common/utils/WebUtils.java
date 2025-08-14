package com.example.flightapi.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.flightapi.common.exception.handler.ApiResult;
import com.example.flightapi.common.exception.SystemException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class WebUtils {

    /**
     * 设置HttpServletResponse
     *
     * @param response    HttpServletResponse
     * @param value       响应内容
     */
    public static void makeResponse(HttpServletResponse response, ApiResult<?> value) {
        String body = "";
        if (value != null) {
            body = JsonUtils.toJson(value);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.OK.value());
        try (OutputStream os = response.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    /**
     * 设置响应
     *
     * @param response    HttpServletResponse
     * @param contentType content-type
     * @param status      http状态码
     * @param value       响应内容
     * @throws IOException IOException
     */
    public static void makeResponse(HttpServletResponse response, String contentType,
                                    int status, Object value) throws IOException {
        response.setContentType(contentType);
        response.setStatus(status);
        OutputStream os = response.getOutputStream();
        os.write(JSONObject.toJSONString(value).getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
    }

    public static void makeFailureResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        OutputStream os = response.getOutputStream();
        os.write(message.getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
    }
}
