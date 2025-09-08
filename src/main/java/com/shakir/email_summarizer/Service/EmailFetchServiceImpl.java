package com.shakir.email_summarizer.Service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;

import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.shakir.email_summarizer.Repository.UserRepository;
import com.shakir.email_summarizer.Response.EmailOverviewResponse;
import com.shakir.email_summarizer.Response.EmailsFetchResponse;
import com.shakir.email_summarizer.Util.FindAuthenticatedUser;
import com.shakir.email_summarizer.Util.GmailService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;


@Service
public class EmailFetchServiceImpl implements EmailFetchService{

    private final EmailConnectService emailConnectService;
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final UserRepository userRepository;
    private final GmailService gmailService;


    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public EmailFetchServiceImpl(EmailConnectService emailConnectService, FindAuthenticatedUser findAuthenticatedUser, UserRepository userRepository, GmailService gmailService) {
        this.emailConnectService = emailConnectService;
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.userRepository = userRepository;
        this.gmailService = gmailService;
    }

    @Transactional(readOnly = true)
    @Override
    public EmailsFetchResponse fetchMail(String pageToken) throws GeneralSecurityException, IOException {
        Gmail gmail = gmailService.getGmailService();
        ListMessagesResponse response = gmail.users().messages().list("me").setPageToken(pageToken)
                .setMaxResults(10L).execute();
        String nextPageToken = response.getNextPageToken();
        List<EmailOverviewResponse> emails = new ArrayList<>();

        if(response.getMessages() != null){
            List<Message> messages = response.getMessages();
            for(Message message : messages){
                Message fullMessage = gmail.users().messages().get("me",message.getId()).execute();

                String subject = "";
                for(MessagePartHeader header : fullMessage.getPayload().getHeaders()){
                    if("Subject".equalsIgnoreCase(header.getName())){
                        subject = header.getValue();
                        break;

                    }
                }
                EmailOverviewResponse emailOverview = new EmailOverviewResponse(message.getId(),subject);
                emails.add(emailOverview);
            }
        }
        return new EmailsFetchResponse(nextPageToken,emails);
    }


}
