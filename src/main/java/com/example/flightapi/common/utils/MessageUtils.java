package com.example.flightapi.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MessageUtils {

    @Autowired
    private MessageSource messageSource;

    /**
     * 获取消息
     *
     * @param code 消息代码
     * @return 消息内容
     */
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    /**
     * 获取带参数的消息
     *
     * @param code 消息代码
     * @param args 参数数组
     * @return 格式化后的消息内容
     */
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * 获取带参数的消息
     *
     * @param clazz 类名
     * @param field 字段
     * @param val   内容
     * @return 格式化后的消息内容
     */
    public String getEntityExistMessage(Class clazz, String field, String val) {
        String entity = StringUtils.capitalize(clazz.getSimpleName());
        return getMessage("common.entity.existed", entity, field, val);
    }

    /**
     * 获取带参数的消息
     *
     * @param clazz 类名
     * @param field 字段
     * @param val   内容
     * @return 格式化后的消息内容
     */
    public String getEntityNotFoundMessage(Class clazz, String field, String val) {
        String entity = StringUtils.capitalize(clazz.getSimpleName());
        return getMessage("common.entity.not.found", entity, field, val);
    }
}
