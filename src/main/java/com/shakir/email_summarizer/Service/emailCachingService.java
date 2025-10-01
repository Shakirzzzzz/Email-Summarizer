package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Entity.Email;

public interface emailCachingService {

    void storeInCache(String emailId, String subject, String messageBody);
    boolean isCached(String emailId);
    Email findCachedEmailById(String emailId);
}
