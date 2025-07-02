package com.starboost.starboost_backend_demo.controller.user;

import com.starboost.starboost_backend_demo.dto.ClaimDto;
import com.starboost.starboost_backend_demo.dto.UserDto;
import com.starboost.starboost_backend_demo.service.ClaimService;
import com.starboost.starboost_backend_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/claims")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserClaimController {
    private final ClaimService claimSvc;
    private final UserService  userSvc;

    @PostMapping
    public ResponseEntity<ClaimDto> create(@RequestBody ClaimDto dto) {
        UserDto me = userSvc.getCurrentUser();
        return ResponseEntity.ok(claimSvc.create(me.getId(), dto));
    }

    @GetMapping
    public List<ClaimDto> list(@RequestParam(defaultValue="all") String dateFilter) {
        UserDto me = userSvc.getCurrentUser();
        return claimSvc.listUser(me.getId(), dateFilter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDto> getOne(@PathVariable Long id) {
        UserDto me = userSvc.getCurrentUser();
        return ResponseEntity.ok(claimSvc.getUserClaim(me.getId(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaimDto> update(
            @PathVariable Long id,
            @RequestBody ClaimDto dto
    ) {
        UserDto me = userSvc.getCurrentUser();
        return ResponseEntity.ok(
                claimSvc.updateUserClaim(me.getId(), id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        UserDto me = userSvc.getCurrentUser();
        claimSvc.deleteUserClaim(me.getId(), id);
        return ResponseEntity.ok().build();
    }
}
