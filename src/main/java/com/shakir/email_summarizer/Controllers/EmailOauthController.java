package com.shakir.email_summarizer.Controllers;

import com.shakir.email_summarizer.Response.RedirectLinkResponse;
import com.shakir.email_summarizer.Service.EmailConnectService;
import com.shakir.email_summarizer.Util.FindAuthenticatedUser;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails/oauth2")
public class EmailOauthController {
    private final EmailConnectService emailConnectService;

    public EmailOauthController(EmailConnectService emailConnectService, FindAuthenticatedUser authenticatedUser) {
        this.emailConnectService = emailConnectService;
    }

    @Operation(summary = "Connect the Gmail to your Account", description = "Authenticate via Google login to connect your email to the app")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/login")
    public RedirectLinkResponse login() throws Exception{
        String authorizationUrl = emailConnectService.getAuthorizationUrl();
        return new RedirectLinkResponse(authorizationUrl);
    }


    @Hidden
    @GetMapping("/callback")
    public String handleCallback(@RequestParam("code") String code, @RequestParam("state") String state) throws Exception{
        emailConnectService.fetchTokens(code,state);
        return "Email Connected. You may close this window now";

    }


}
