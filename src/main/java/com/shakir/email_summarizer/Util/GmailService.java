package com.shakir.email_summarizer.Util;

import com.google.api.services.gmail.Gmail;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GmailService {
    Gmail getGmailService() throws GeneralSecurityException, IOException;
}
