package com.competition.backend.service;

import com.competition.backend.vo.NotificationVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    // 获取通知列表（可根据状态筛选）
    Page<NotificationVO> getMyNotifications(Long userId, Boolean isRead, Pageable pageable);

    // 标记某条通知为已读
    void markAsRead(Long userId, Long notificationId);

    // 一键标记全部已读
    void markAllAsRead(Long userId);

    // 获取未读数量
    long getUnreadCount(Long userId);
}