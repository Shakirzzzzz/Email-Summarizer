package com.shakir.email_summarizer.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

public class RegisterRequest {

    @NotEmpty(message = "First name is Mandatory")
    @Size(min=3,max = 30, message = "First name must be at least 3 char long")
    private String firstName;

    @NotEmpty(message = "Last name is Mandatory")
    @Size(min=3,max = 30, message = "Last name must be at least 3 char long")
    private String lastName;

    @NotEmpty(message = "Email is Mandatory")
    @Email
    private String email;

    @NotEmpty(message = "Password is Mandatory")
    @Size(min=5,max = 30, message = "Password must be at least 5 char long")
    private String password;

    public RegisterRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
