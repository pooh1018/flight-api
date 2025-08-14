package com.example.flightapi.common.controller;

import com.example.flightapi.common.utils.MessageUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 消息演示控制器
 * 用于展示如何使用带变量的国际化消息
 */
@RestController
@RequestMapping("/public/messages")
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
    @GetMapping("/operation")
    public String getOperationMessage(@RequestParam String name) {
        return messageUtils.getMessage("common.operation.success", name);
    }

    /**
     * 获取带多个变量的消息
     */
    @GetMapping("/entity/{entityType}/{fieldName}/{fieldValue}")
    public String getEntityNotFoundMessage(
            @PathVariable String entityType,
            @PathVariable String fieldName,
            @PathVariable String fieldValue) {
        return messageUtils.getMessage("common.entity.not.found", entityType, fieldName, fieldValue);
    }
}
