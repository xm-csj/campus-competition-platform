package com.competition.backend.controller;

import com.competition.backend.common.result.Result;
import com.competition.backend.service.NotificationService;
import com.competition.backend.util.SecurityUtil;
import com.competition.backend.vo.NotificationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "通知模块")
@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "查询我的通知列表", description = "可传 isRead=false 查未读，isRead=true 查已读，不传查全部")
    @GetMapping
    public Result<Page<NotificationVO>> getMyNotifications(
            @RequestParam(required = false) Boolean isRead,
            Pageable pageable) {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(notificationService.getMyNotifications(userId, isRead, pageable));
    }

    @Operation(summary = "获取未读通知数量")
    @GetMapping("/unread-count")
    public Result<Long> getUnreadCount() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(notificationService.getUnreadCount(userId));
    }

    @Operation(summary = "标记单条通知为已读")
    @PutMapping("/{id}/read")
    public Result<Void> markAsRead(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        notificationService.markAsRead(userId, id);
        return Result.success();
    }

    @Operation(summary = "一键标记所有未读为已读")
    @PutMapping("/read-all")
    public Result<Void> markAllAsRead() {
        Long userId = SecurityUtil.getCurrentUserId();
        notificationService.markAllAsRead(userId);
        return Result.success();
    }
}