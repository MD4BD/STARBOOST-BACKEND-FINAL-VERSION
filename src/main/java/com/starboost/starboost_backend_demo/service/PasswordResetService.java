
package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.ForgotPasswordRequest;
import com.starboost.starboost_backend_demo.dto.ResetPasswordRequest;


public interface PasswordResetService {
    
    void forgotPassword(ForgotPasswordRequest request);

    
    void resetPassword(String token, ResetPasswordRequest request);
}
