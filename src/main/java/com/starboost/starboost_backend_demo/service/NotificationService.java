
package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.NotificationDto;
import java.time.LocalDateTime;
import java.util.List;


public interface NotificationService {

    
    NotificationDto create(NotificationDto dto);

    
    List<NotificationDto> listAll();

    
    List<NotificationDto> findPending(LocalDateTime now);

    
    void deliver(NotificationDto dto);

    
    void dispatchPending();

    
    NotificationDto getById(Long id);

    
    NotificationDto update(NotificationDto dto);

    
    void deleteById(Long id);

    
    boolean isReadBy(Long notificationId, Long userId);

    
    void markAsRead(Long notificationId, Long userId);

}
