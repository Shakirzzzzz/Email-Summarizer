package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Response.EmailsFetchResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface EmailFetchService {

    EmailsFetchResponse fetchMail(String pageToken) throws GeneralSecurityException, IOException;

}
