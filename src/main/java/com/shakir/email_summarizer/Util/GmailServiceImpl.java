package com.shakir.email_summarizer.Util;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.shakir.email_summarizer.Entity.User;
import com.shakir.email_summarizer.Repository.UserRepository;
import com.shakir.email_summarizer.Service.EmailConnectService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;

@Component
public class GmailServiceImpl implements GmailService {
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final UserRepository userRepository;
    private final EmailConnectService emailConnectService;

    @Value("${google.oauth.clientId}")
    private String clientId;

    @Value("${google.oauth.clientSecret}")
    private String clientSecret;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public GmailServiceImpl(FindAuthenticatedUser findAuthenticatedUser, UserRepository userRepository, EmailConnectService emailConnectService) {
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.userRepository = userRepository;
        this.emailConnectService = emailConnectService;
    }

    @Override
    public Gmail getGmailService() throws GeneralSecurityException, IOException {
        User user = findAuthenticatedUser.getAuthenticatedUser();

        if(user.getAccessToken() == null || user.getRefreshToken() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You need to connect your email first");
        }

        if (Instant.now().isAfter(user.getAccessTokenExpiry()) ){
            emailConnectService.refreshAccessToken();
            user = userRepository.findById(user.getId()).get();
        }

        final NetHttpTransport HTTP_TRANSPORT;
        try {
            HTTP_TRANSPORT = new NetHttpTransport();
        } catch (Throwable t) {
            throw new RuntimeException("Failed to initialize HTTP transport", t);
        }
        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret))
                .setTokenServerEncodedUrl("https://oauth2.googleapis.com/token")
                .build()
                .setAccessToken(user.getAccessToken())
                .setRefreshToken(user.getRefreshToken());


        return new Gmail.Builder(HTTP_TRANSPORT,JSON_FACTORY,credential).setApplicationName("Email Summarizer").build();
    }
}
