package com.shakir.email_summarizer.Response;

import com.shakir.email_summarizer.Entity.Authority;

import java.util.List;

public class UserResponse {
    private long id;
    private String fullName;
    private String email;

    private List<Authority> authorities;

    public UserResponse(String fullName, long id, String email, List<Authority> authorities) {
        this.fullName = fullName;
        this.id = id;
        this.email = email;
        this.authorities = authorities;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
