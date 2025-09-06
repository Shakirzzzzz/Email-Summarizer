package com.shakir.email_summarizer.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdatePasswordRequest {
    @NotEmpty(message = "Old Password is Mandatory")
    @Size(min=5,max = 30, message = " Old Password must be at least 5 char long")
    private String oldPassword;

    @NotEmpty(message = "New Password is Mandatory")
    @Size(min=5,max = 30, message = " New Password must be at least 5 char long")
    private String newPassword;

    @NotEmpty(message = "Confirmed Password is Mandatory")
    @Size(min=5,max = 30, message = "Confirmed Password must be at least 5 char long")
    private String newPassword2;

    public UpdatePasswordRequest(String oldPassword, String newPassword, String newPassword2) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPassword2 = newPassword2;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }
}
