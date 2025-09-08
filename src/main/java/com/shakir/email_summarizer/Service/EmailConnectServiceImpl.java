package com.shakir.email_summarizer.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.shakir.email_summarizer.Entity.User;
import com.shakir.email_summarizer.Repository.UserRepository;
import com.shakir.email_summarizer.Util.FindAuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class EmailConnectServiceImpl implements EmailConnectService{

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

    @Override
    public void fetchTokens(String code, String state) throws Exception {
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
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
        userRepository.save(new_user);

    }


}
