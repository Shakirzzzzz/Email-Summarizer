package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Entity.Email;
import com.shakir.email_summarizer.Response.CachedEmailsResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface emailCachingService {

    void storeInCache(String emailId, String subject, String messageBody) throws  AccessDeniedException;
    boolean isCached(String emailId);
    Email findCachedEmailById(String emailId);
    CachedEmailsResponse getCachedEmailsById(int PageNumber) throws AccessDeniedException;
}
