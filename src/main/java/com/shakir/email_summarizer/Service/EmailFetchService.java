package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Response.EmailsFetchResponse;
import com.shakir.email_summarizer.Response.FullEmailResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface EmailFetchService {

    EmailsFetchResponse fetchMails(String pageToken) throws GeneralSecurityException, IOException;
    FullEmailResponse fetchMailById(String emailId) throws GeneralSecurityException, IOException;
}
