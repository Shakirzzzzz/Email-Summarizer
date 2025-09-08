package com.shakir.email_summarizer.Service;

public interface EmailConnectService {

    String getAuthorizationUrl() throws Exception;
    void fetchTokens(String code, String state) throws Exception;
}
