package com.competition.backend.service.impl;

import com.competition.backend.common.constant.ErrorCode;
import com.competition.backend.common.exception.BusinessException;
import com.competition.backend.entity.SysNotification;
import com.competition.backend.repository.SysNotificationRepository;
import com.competition.backend.service.NotificationService;
import com.competition.backend.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SysNotificationRepository notificationRepository;

    @Override
    public Page<NotificationVO> getMyNotifications(Long userId, Boolean isRead, Pageable pageable) {
        Page<SysNotification> records;
        if (isRead == null) {
            // 如果不传 isRead 参数，查询所有
            records = notificationRepository.findByReceiverId(userId, pageable);
        } else {
            // 根据已读/未读状态查询
            records = notificationRepository.findByReceiverIdAndIsRead(userId, isRead, pageable);
        }
        return records.map(this::convertToVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long userId, Long notificationId) {
        SysNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "通知不存在"));

        // 越权校验：只能标记自己的通知
        if (!notification.getReceiverId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作他人的通知");
        }

        // 如果未读，则标记为已读
        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadByReceiverId(userId);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByReceiverIdAndIsRead(userId, false);
    }

    private NotificationVO convertToVO(SysNotification entity) {
        return NotificationVO.builder()
                .id(entity.getId())
                .type(entity.getType())
                .title(entity.getTitle())
                .content(entity.getContent())
                .relatedId(entity.getRelatedId())
                .isRead(entity.getIsRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}