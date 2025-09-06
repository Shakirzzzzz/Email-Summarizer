package com.shakir.email_summarizer.Service;


import com.shakir.email_summarizer.Request.AuthenticationRequest;
import com.shakir.email_summarizer.Request.RegisterRequest;
import com.shakir.email_summarizer.Response.AuthenticationResponse;

public interface AuthenticationService {


    void register(RegisterRequest input) throws Exception;

    AuthenticationResponse login(AuthenticationRequest request);
}
