package com.shakir.email_summarizer.Controllers;

import com.shakir.email_summarizer.Request.AuthenticationRequest;
import com.shakir.email_summarizer.Request.RegisterRequest;
import com.shakir.email_summarizer.Response.AuthenticationResponse;
import com.shakir.email_summarizer.Service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Endpoints", description = "Operations related to Registration and Login")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    @Operation(summary = "Register a new User", description = "Creates a new User in the Database.")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) throws Exception{
        authenticationService.register(registerRequest);
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    @Operation(summary = "Login an Existing User", description = "Submit the email and password used during the registration")
    public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest authenticationRequest){
        return authenticationService.login(authenticationRequest);
    }
}
