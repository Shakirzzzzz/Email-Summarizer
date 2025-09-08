package com.shakir.email_summarizer.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.shakir.email_summarizer.Entity.User;
import com.shakir.email_summarizer.Repository.UserRepository;
import com.shakir.email_summarizer.Util.FindAuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Optional;


@Service
public class EmailConnectServiceImpl implements EmailConnectService{

    @Value("${google.oauth.clientId}")
    private String clientId;

    @Value("${google.oauth.clientSecret}")
    private String clientSecret;

    @Value("${google.oauth.redirect-uri}")
    private String redirectURI;


    private final UserRepository userRepository;
    private final GoogleAuthorizationCodeFlow flow;
    private final FindAuthenticatedUser authenticatedUser;

    @Autowired
    public EmailConnectServiceImpl(UserRepository userRepository, GoogleAuthorizationCodeFlow flow, FindAuthenticatedUser authenticatedUser) {
        this.userRepository = userRepository;
        this.flow = flow;
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public String getAuthorizationUrl() throws Exception {
        User user = authenticatedUser.getAuthenticatedUser();
        return flow.newAuthorizationUrl().setApprovalPrompt("force").setState(String.valueOf(user.getId()))
                .setRedirectUri(redirectURI).build();
    }
    @Transactional
    @Override
    public void fetchTokens(String code, String state) throws Exception {
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        Long expiresInSeconds = tokenResponse.getExpiresInSeconds();
        Instant expiryTime = Instant.now().plusSeconds(expiresInSeconds);
        Long userId = Long.parseLong(state);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new IllegalArgumentException("Incorrect user id" + userId);
        }
        User new_user = user.get();
        if(refreshToken != null){
            new_user.setRefreshToken(refreshToken);
        }
        new_user.setAccessToken(accessToken);
        new_user.setAccessTokenExpiry(expiryTime);
        userRepository.save(new_user);

    }
    @Override
    @Transactional
    public void refreshAccessToken() throws IOException {
        User user = authenticatedUser.getAuthenticatedUser();
        if(user.getRefreshToken() == null) {
            throw new IllegalStateException("No refresh token available for this user");
        }

        final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        GoogleTokenResponse tokenResponse = new GoogleRefreshTokenRequest(
                HTTP_TRANSPORT,
                GsonFactory.getDefaultInstance(),
                user.getRefreshToken(),
                clientId,
                clientSecret
        ).execute();

        user.setAccessToken(tokenResponse.getAccessToken());
        userRepository.save(user);
    }



}
