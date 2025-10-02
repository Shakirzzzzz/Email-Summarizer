package com.shakir.email_summarizer.Controllers;


import com.shakir.email_summarizer.Response.CachedEmailsResponse;
import com.shakir.email_summarizer.Response.EmailsFetchResponse;
import com.shakir.email_summarizer.Response.FullEmailResponse;
import com.shakir.email_summarizer.Service.EmailFetchService;
import com.shakir.email_summarizer.Service.emailCachingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.GeneralSecurityException;

@Tag(name="Email Usage Endpoints", description = "Operations related to fetching and viewing emails")
@RestController
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailFetchService emailFetchService;
    private final emailCachingService emailCachingService;

    public EmailController(EmailFetchService emailFetchService, emailCachingService emailCachingService) {
        this.emailFetchService = emailFetchService;
        this.emailCachingService = emailCachingService;
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

    @GetMapping("/cached")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetches 10 Recent Cached emails", description = "Fetches 10 most recent emails cached in a pagination Style")
    public CachedEmailsResponse fetchCachedEmails(@RequestParam(required = true) int pageNumber) throws AccessDeniedException {
        return emailCachingService.getCachedEmailsById(pageNumber);

    }

}
