package com.shakir.email_summarizer.Controllers;


import com.shakir.email_summarizer.Response.EmailsFetchResponse;
import com.shakir.email_summarizer.Response.FullEmailResponse;
import com.shakir.email_summarizer.Service.EmailFetchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.boot.model.source.spi.IdentifierSourceSimple;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Tag(name="Email Usage Endpoints", description = "Operations related to fetching and viewing emails")
@RestController
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailFetchService emailFetchService;

    public EmailController(EmailFetchService emailFetchService) {
        this.emailFetchService = emailFetchService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetches 10 email heading at a time", description = "Gets 10 emails id's and and Subjects in a pagination Style")
    public EmailsFetchResponse getEmails(@RequestParam(required = false) String pageToken) throws GeneralSecurityException, IOException {
        return emailFetchService.fetchMails(pageToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{emailId}")
    @Operation(summary = "Fetches a single emails using email Id", description = "Gets a single email with full email Body")
    public FullEmailResponse fetchEmailById(@PathVariable String emailId) throws GeneralSecurityException, IOException {
        return emailFetchService.fetchMailById(emailId);
    }

}
