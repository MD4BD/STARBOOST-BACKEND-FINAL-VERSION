package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.NotificationDto;
import com.starboost.starboost_backend_demo.entity.Notification;
import com.starboost.starboost_backend_demo.entity.NotificationRead;
import com.starboost.starboost_backend_demo.repository.NotificationReadRepository;
import com.starboost.starboost_backend_demo.repository.NotificationRepository;
import com.starboost.starboost_backend_demo.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;
    private final NotificationReadRepository readRepo;

    @Override
    public NotificationDto create(NotificationDto dto) {
        Notification n = Notification.builder()
                .title(dto.getTitle())
                .message(dto.getMessage())
                .targetType(dto.getTargetType())
                .targetValue(dto.getTargetValue())
                .createdAt(LocalDateTime.now())
                .sendAt(dto.getSendAt() != null ? dto.getSendAt() : LocalDateTime.now())
                .sent(false)
                .build();
        return toDto(repo.save(n));
    }

    @Override
    public List<NotificationDto> listAll() {
        return repo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDto getById(Long id) {
        return repo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found: " + id));
    }

    @Override
    public NotificationDto update(NotificationDto dto) {
        Notification n = repo.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Notification not found: " + dto.getId()));
        n.setTitle(dto.getTitle());
        n.setMessage(dto.getMessage());
        n.setTargetType(dto.getTargetType());
        n.setTargetValue(dto.getTargetValue());
        n.setSendAt(dto.getSendAt());
        return toDto(repo.save(n));
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<NotificationDto> findPending(LocalDateTime now) {
        return repo.findPendingNotifications(now)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deliver(NotificationDto dto) {
        Notification n = repo.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Notification not found " + dto.getId()));
        n.setSent(true);
        repo.save(n);
    }

    @Override
    @Scheduled(cron = "0 * * * * *")  // every minute
    public void dispatchPending() {
        LocalDateTime now = LocalDateTime.now();
        findPending(now).forEach(this::deliver);
    }


    @Override
    public boolean isReadBy(Long notificationId, Long userId) {
        return readRepo.existsByNotificationIdAndUserId(notificationId, userId);
    }


    @Override
    public void markAsRead(Long notificationId, Long userId) {
        if (!readRepo.existsByNotificationIdAndUserId(notificationId, userId)) {
            NotificationRead nr = NotificationRead.builder()
                    .notificationId(notificationId)
                    .userId(userId)
                    .readAt(LocalDateTime.now())
                    .build();
            readRepo.save(nr);
        }
    }
    private NotificationDto toDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .targetType(n.getTargetType())
                .targetValue(n.getTargetValue())
                .createdAt(n.getCreatedAt())
                .sendAt(n.getSendAt())
                .sent(n.isSent())
                .read(false)           
                .build();
    }

}

