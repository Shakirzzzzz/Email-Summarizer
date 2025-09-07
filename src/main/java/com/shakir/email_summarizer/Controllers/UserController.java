package com.shakir.email_summarizer.Controllers;


import com.shakir.email_summarizer.Request.UpdatePasswordRequest;
import com.shakir.email_summarizer.Response.UserResponse;
import com.shakir.email_summarizer.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/users")
@Tag(name="User Actions Endpoints", description = "Operations that current logged in user can perform")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "User Info", description = "Gets the info about the current User")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/info")
    public UserResponse getUserInfo() throws AccessDeniedException {
        return userService.getUserInfo();

    }

    @Operation(summary = "Delete User(Self)", description = "Current User can delete himself from the database")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void deleteUser() throws  AccessDeniedException{
        userService.deleteUser();
    }


    @Operation(summary = "Password Update", description = "User can change his/her password")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/password")
    public void passwordUpdate(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) throws Exception{
        userService.updatePassword(updatePasswordRequest);
    }


}
