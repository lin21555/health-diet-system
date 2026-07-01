package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.entity.Message;
import com.example.healthdiet.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/list")
    public Result<List<Message>> list() {
        return Result.success(messageService.lambdaQuery().orderByDesc(Message::getCreateTime).list());
    }

    @PostMapping
    public Result<Message> add(@RequestBody Message message) {
        message.setStatus("未回复");
        messageService.save(message);
        return Result.success(message);
    }

    @PutMapping("/reply/{id}")
    public Result<Boolean> reply(@PathVariable Long id, @RequestBody Message message) {
        Message old = messageService.getById(id);
        if (old == null) {
            return Result.error("留言不存在");
        }
        old.setReply(message.getReply());
        old.setStatus("已回复");
        old.setReplyTime(LocalDateTime.now());
        return Result.success(messageService.updateById(old));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(messageService.removeById(id));
    }
}
