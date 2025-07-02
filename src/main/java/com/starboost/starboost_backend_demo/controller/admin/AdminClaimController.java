package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.ClaimDto;
import com.starboost.starboost_backend_demo.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/claims")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminClaimController {
    private final ClaimService claimSvc;

    @GetMapping
    public List<ClaimDto> list(
            @RequestParam(defaultValue="all")   String dateFilter,
            @RequestParam(defaultValue="all")   String readFilter
    ) {
        return claimSvc.listAdmin(dateFilter, readFilter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDto> getOne(@PathVariable Long id) {
        ClaimDto dto = claimSvc.getAdminClaim(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id) {
        claimSvc.markRead(id);
        return ResponseEntity.ok().build();
    }
}
