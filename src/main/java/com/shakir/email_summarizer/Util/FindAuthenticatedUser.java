package com.shakir.email_summarizer.Util;

import com.shakir.email_summarizer.Entity.User;

import java.nio.file.AccessDeniedException;

public interface FindAuthenticatedUser {
    User getAuthenticatedUser() throws AccessDeniedException;
}
