package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Response.UserResponse;

import java.util.List;

public interface AdminService {

    List<UserResponse> getAllUsers();
    UserResponse promoteToAdmin(long userId);
    void deleteNonAdminUser(long id);
}
