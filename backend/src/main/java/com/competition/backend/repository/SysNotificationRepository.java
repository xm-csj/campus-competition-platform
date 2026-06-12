package com.competition.backend.repository;

import com.competition.backend.entity.SysNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SysNotificationRepository extends JpaRepository<SysNotification, Long> {

    // 查询我的所有通知
    Page<SysNotification> findByReceiverId(Long receiverId, Pageable pageable);

    // 查询我的通知（根据是否已读过滤）
    Page<SysNotification> findByReceiverIdAndIsRead(Long receiverId, Boolean isRead, Pageable pageable);

    // 统计我的未读数量
    long countByReceiverIdAndIsRead(Long receiverId, Boolean isRead);

    // 一键标记我的所有未读通知为已读
    @Modifying
    @Query("UPDATE SysNotification n SET n.isRead = true WHERE n.receiverId = :receiverId AND n.isRead = false")
    int markAllAsReadByReceiverId(@Param("receiverId") Long receiverId);
}