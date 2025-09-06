package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Request.UpdatePasswordRequest;
import com.shakir.email_summarizer.Response.UserResponse;

import java.nio.file.AccessDeniedException;

public interface UserService {
    UserResponse getUserInfo() throws AccessDeniedException;
    void deleteUser() throws AccessDeniedException;
    void updatePassword(UpdatePasswordRequest updatePasswordRequest) throws AccessDeniedException;
}
