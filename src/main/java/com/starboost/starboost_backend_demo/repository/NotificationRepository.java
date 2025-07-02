package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;  
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
        SELECT n
          FROM Notification n
         WHERE n.sent = false
           AND (n.sendAt IS NULL OR n.sendAt <= :now)
        """)
    List<Notification> findPendingNotifications(@Param("now") LocalDateTime now);
}
