// src/main/java/com/starboost/starboost_backend_demo/scheduler/NotificationDispatcher.java
package com.starboost.starboost_backend_demo.scheduler;

import com.starboost.starboost_backend_demo.dto.NotificationDto;
import com.starboost.starboost_backend_demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class NotificationDispatcher {

    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * *")
    public void dispatch() {
        LocalDateTime now = LocalDateTime.now();
        List<NotificationDto> pending = notificationService.findPending(now);

        for (NotificationDto dto : pending) {
            try {
                notificationService.deliver(dto);
            } catch (Exception ex) {
                System.err.printf(
                        "❌ Failed to deliver notification %d: %s%n",
                        dto.getId(), ex.getMessage()
                );
            }
        }
    }
}
