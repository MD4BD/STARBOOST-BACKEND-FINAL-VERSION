package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.NotificationRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationReadRepository extends JpaRepository<NotificationRead, Long> {
    boolean existsByNotificationIdAndUserId(Long notificationId, Long userId);
}
