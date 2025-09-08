package com.shakir.email_summarizer.Service;

import java.io.IOException;

public interface EmailConnectService {

    String getAuthorizationUrl() throws Exception;
    void fetchTokens(String code, String state) throws Exception;
    void refreshAccessToken() throws IOException;
}
