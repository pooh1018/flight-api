package com.example.flightapi.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息演示控制器
 * 用于展示如何使用带变量的国际化消息
 */
@RestController
@RequestMapping("/api/demo/messages")
public class MessageDemoController {

    private final MessageUtils messageUtils;

    public MessageDemoController(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    /**
     * 获取简单消息
     */
    @GetMapping("/simple")
    public String getSimpleMessage() {
        return messageUtils.getMessage("common.success");
    }

    /**
     * 获取带一个变量的消息
     */
    @GetMapping("/operation/{name}")
    public String getOperationMessage(@PathVariable String name) {
        return messageUtils.getMessage("common.operation.success", name);
    }

    /**
     * 获取带多个变量的消息
     */
//    @GetMapping("/entity/{entityType}/{fieldName}/{fieldValue}")
    public String getEntityNotFoundMessage(
            @PathVariable String entityType,
            @PathVariable String fieldName,
            @PathVariable String fieldValue) {
        return messageUtils.getMessage("common.entity.not.found", entityType, fieldName, fieldValue);
    }
}
