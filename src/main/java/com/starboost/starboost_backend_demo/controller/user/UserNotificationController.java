package com.starboost.starboost_backend_demo.controller.user;

import com.starboost.starboost_backend_demo.dto.NotificationDto;
import com.starboost.starboost_backend_demo.dto.UserDto;
import com.starboost.starboost_backend_demo.service.NotificationService;
import com.starboost.starboost_backend_demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/notifications")
@PreAuthorize("hasRole('USER')")
public class UserNotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public UserNotificationController(NotificationService notificationService,
                                      UserService userService) {
        this.notificationService = notificationService;
        this.userService         = userService;
    }

    @GetMapping
    public List<NotificationDto> list(
            @RequestParam(defaultValue = "all") String dateFilter,
            @RequestParam(defaultValue = "all") String readFilter
    ) {
        UserDto me = userService.getCurrentUser();
        List<NotificationDto> all = notificationService.listAll();

        List<NotificationDto> mine = all.stream()
                .filter(NotificationDto::isSent)
                .filter(n -> n.getSendAt() == null
                        || n.getSendAt().isBefore(java.time.LocalDateTime.now()))
                .filter(n -> {
                    if ("USER".equals(n.getTargetType())) {
                        return n.getTargetValue().equals(me.getId().toString());
                    } else {
                        return List.of(n.getTargetValue().split(","))
                                .contains(me.getRole());
                    }
                })
                .collect(Collectors.toList());

        if ("today".equalsIgnoreCase(dateFilter)) {
            LocalDate today = LocalDate.now(ZoneId.systemDefault());
            mine = mine.stream()
                    .filter(n -> {
                        LocalDate d = n.getSendAt()!=null
                                ? n.getSendAt().toLocalDate()
                                : n.getCreatedAt().toLocalDate();
                        return d.equals(today);
                    })
                    .collect(Collectors.toList());
        }

        if ("unread".equalsIgnoreCase(readFilter)) {
            mine = mine.stream()
                    .filter(n -> !notificationService.isReadBy(n.getId(), me.getId()))
                    .collect(Collectors.toList());
        }

        mine.forEach(n -> n.setRead(notificationService.isReadBy(n.getId(), me.getId())));
        return mine;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getOne(@PathVariable Long id) {
        UserDto me = userService.getCurrentUser();
        NotificationDto dto = notificationService.getById(id);

        boolean ok = dto.isSent()
                && (("USER".equals(dto.getTargetType())
                && dto.getTargetValue().equals(me.getId().toString()))
                || ("ROLE".equals(dto.getTargetType())
                && List.of(dto.getTargetValue().split(","))
                .contains(me.getRole())));

        if (!ok) {
            return ResponseEntity.status(403).build();
        }

        notificationService.markAsRead(id, me.getId());

        dto.setRead(true);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id) {
        UserDto me = userService.getCurrentUser();
        notificationService.markAsRead(id, me.getId());
        return ResponseEntity.ok().build();
    }
}
