package com.shakir.email_summarizer.Service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;

import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.shakir.email_summarizer.Entity.Email;
import com.shakir.email_summarizer.Repository.UserRepository;
import com.shakir.email_summarizer.Response.EmailOverviewResponse;
import com.shakir.email_summarizer.Response.EmailsFetchResponse;
import com.shakir.email_summarizer.Response.FullEmailResponse;
import com.shakir.email_summarizer.Util.FindAuthenticatedUser;
import com.shakir.email_summarizer.Util.GmailService;

import java.util.Base64;


import org.jsoup.Jsoup;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;

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
    private final ChatModel chatModel;
    private final emailCachingService emailCachingService;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Autowired
    public EmailFetchServiceImpl(EmailConnectService emailConnectService, FindAuthenticatedUser findAuthenticatedUser, UserRepository userRepository, GmailService gmailService, ChatModel chatModel, emailCachingService emailCachingService) {
        this.emailConnectService = emailConnectService;
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.userRepository = userRepository;
        this.gmailService = gmailService;
        this.chatModel = chatModel;
        this.emailCachingService = emailCachingService;
    }

    @Transactional(readOnly = true)
    @Override
    public EmailsFetchResponse fetchMails(String pageToken) throws GeneralSecurityException, IOException {
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

    public FullEmailResponse fetchMailById(String emailId) throws GeneralSecurityException, IOException {
        if(emailCachingService.isCached(emailId)){
            Email cachedEmail = emailCachingService.findCachedEmailById(emailId);
            FullEmailResponse emailResponse = new FullEmailResponse(cachedEmail.getEmailId(), cachedEmail.getSubject(), cachedEmail.getMessageBody());
            return emailResponse;
        }
        Gmail gmail = gmailService.getGmailService();
        Message fullMessage = gmail.users().messages().get("me",emailId).setFormat("full")
                .execute();
        MessagePart payload = fullMessage.getPayload();


        String subject = "";
        for(MessagePartHeader header : payload.getHeaders()){
            if("Subject".equalsIgnoreCase(header.getName())){
                subject = header.getValue();
                break;
            }
        }
        String bodyText = extractTextFromPayload(payload);
        String text = Jsoup.parse(bodyText).text();
        String message = "Following will be message extracted from my email. Summarize it and give as brief as possible description while keeping the important info in. Dont Describe Stuff : " + text;

        String reply =  chatModel.call(message);
        FullEmailResponse emailResponse = new FullEmailResponse(emailId,subject,reply);
        emailCachingService.storeInCache(emailId,subject,reply);
        return emailResponse;
    }

    private String extractTextFromPayload(MessagePart payload) {
        String mimeType = payload.getMimeType();
        if("text/plain".equalsIgnoreCase(mimeType)){
            String data = payload.getBody().getData();
            return new String(Base64.getUrlDecoder().decode(data));
        } else if ("text/html".equalsIgnoreCase(mimeType)) {
            String data = payload.getBody().getData();
            String html = new String(Base64.getUrlDecoder().decode(data));
            return Jsoup.parse(html).html();}
        else if(mimeType.startsWith("multipart/")){
            if(payload.getParts() != null){
                for(MessagePart part : payload.getParts()){
                    String result = extractTextFromPayload(part);
                    if(result != null && !result.isEmpty()){
                        return result;
                    }
                }
            }
        }
        return "";
    }





}
