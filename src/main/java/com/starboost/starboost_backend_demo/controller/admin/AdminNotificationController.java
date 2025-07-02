package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.NotificationDto;
import com.starboost.starboost_backend_demo.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final NotificationService svc;


    @PostMapping
    public ResponseEntity<NotificationDto> create(@Valid @RequestBody NotificationDto dto) {
        NotificationDto created = svc.create(dto);
        return ResponseEntity
                .created(URI.create("/api/admin/notifications/" + created.getId()))
                .body(created);
    }


    @GetMapping
    public ResponseEntity<List<NotificationDto>> listAll() {
        return ResponseEntity.ok(svc.listAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getOne(@PathVariable Long id) {
        NotificationDto dto = svc.getById(id);
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<NotificationDto> update(
            @PathVariable Long id,
            @Valid @RequestBody NotificationDto dto
    ) {
        dto.setId(id);
        NotificationDto updated = svc.update(dto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
