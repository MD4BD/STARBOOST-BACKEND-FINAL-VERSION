package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.ClaimDto;
import com.starboost.starboost_backend_demo.entity.Claim;
import com.starboost.starboost_backend_demo.entity.User;
import com.starboost.starboost_backend_demo.repository.ClaimRepository;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import com.starboost.starboost_backend_demo.service.ClaimService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepo;
    private final UserRepository  userRepo;

    @Override
    public ClaimDto create(Long userId, ClaimDto dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        Claim c = Claim.builder()
                .title(dto.getTitle())
                .message(dto.getMessage())
                .createdAt(LocalDateTime.now())
                .sent(true)
                .read(false)
                .user(user)
                .build();
        return toDto(claimRepo.save(c));
    }

    @Override
    public List<ClaimDto> listUser(Long userId, String dateFilter) {
        LocalDateTime start, end;
        if ("today".equalsIgnoreCase(dateFilter)) {
            LocalDate t = LocalDate.now();
            start = t.atStartOfDay();
            end   = t.plusDays(1).atStartOfDay();
        } else {
            start = LocalDate.of(1970,1,1).atStartOfDay();
            end   = LocalDateTime.now().plusYears(100);
        }
        return claimRepo
                .findByUserIdAndCreatedAtBetween(userId, start, end)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ClaimDto getUserClaim(Long userId, Long claimId) {
        Claim c = claimRepo.findById(claimId)
                .filter(cl -> cl.getUser().getId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Claim not found or not yours: " + claimId));
        return toDto(c);
    }

    @Override
    public ClaimDto updateUserClaim(Long userId, Long claimId, ClaimDto dto) {
        Claim c = claimRepo.findById(claimId)
                .filter(cl -> cl.getUser().getId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Claim not found or not yours: " + claimId));

        c.setTitle(dto.getTitle());
        c.setMessage(dto.getMessage());
        return toDto(claimRepo.save(c));
    }

    @Override
    public void deleteUserClaim(Long userId, Long claimId) {
        Claim c = claimRepo.findById(claimId)
                .filter(cl -> cl.getUser().getId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Claim not found or not yours: " + claimId));
        claimRepo.delete(c);
    }

    @Override
    public List<ClaimDto> listAdmin(String dateFilter, String readFilter) {
        LocalDateTime start, end = LocalDateTime.now().plusYears(100);
        if ("today".equalsIgnoreCase(dateFilter)) {
            LocalDate t = LocalDate.now();
            start = t.atStartOfDay();
        } else {
            start = LocalDate.of(1970,1,1).atStartOfDay();
        }
        boolean onlyUnread = "unread".equalsIgnoreCase(readFilter);
        List<Claim> list = onlyUnread
                ? claimRepo.findByReadFalseAndCreatedAtBetween(start, end)
                : claimRepo.findByCreatedAtBetween(start, end);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ClaimDto getAdminClaim(Long claimId) {
        Claim c = claimRepo.findById(claimId)
                .orElseThrow(() -> new EntityNotFoundException("Claim not found: " + claimId));
        return toDto(c);
    }

    @Override
    public void markRead(Long claimId) {
        Claim c = claimRepo.findById(claimId)
                .orElseThrow(() -> new EntityNotFoundException("Claim not found: " + claimId));
        c.setRead(true);
        claimRepo.save(c);
    }

    private ClaimDto toDto(Claim c) {
        return ClaimDto.builder()
                .id(c.getId())
                .title(c.getTitle())
                .message(c.getMessage())
                .createdAt(c.getCreatedAt())
                .sent(c.isSent())
                .read(c.isRead())
                .userId(c.getUser().getId())
                .build();
    }
}
