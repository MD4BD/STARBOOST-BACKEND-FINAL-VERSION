
package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.ClaimDto;
import java.util.List;

public interface ClaimService {
    
    ClaimDto create(Long userId, ClaimDto dto);

    
    List<ClaimDto> listUser(Long userId, String dateFilter);

    
    ClaimDto getUserClaim(Long userId, Long claimId);

    
    ClaimDto updateUserClaim(Long userId, Long claimId, ClaimDto dto);

    
    void deleteUserClaim(Long userId, Long claimId);

    
    List<ClaimDto> listAdmin(String dateFilter, String readFilter);

    
    ClaimDto getAdminClaim(Long claimId);

    
    void markRead(Long claimId);
}
