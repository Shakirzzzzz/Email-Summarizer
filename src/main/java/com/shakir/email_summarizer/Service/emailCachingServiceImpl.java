package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Entity.Email;
import com.shakir.email_summarizer.Entity.User;
import com.shakir.email_summarizer.Repository.emailRepository;
import com.shakir.email_summarizer.Response.CachedEmailsResponse;
import com.shakir.email_summarizer.Response.EmailOverviewResponse;
import com.shakir.email_summarizer.Util.FindAuthenticatedUser;
import org.hibernate.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class emailCachingServiceImpl implements emailCachingService{
    private final emailRepository emailRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;


    @Autowired
    public emailCachingServiceImpl(emailRepository emailRepository, UserService userService, FindAuthenticatedUser findAuthenticatedUser) {
        this.emailRepository = emailRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
    }


    @Override
    public void storeInCache(String emailId, String subject, String messageBody) throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();
        Email cachedEmail = new Email(emailId,subject,messageBody);
        cachedEmail.setUser(user);
        emailRepository.save(cachedEmail);

    }
    @Override
    public Email findCachedEmailById(String emailId){
        return emailRepository.findById(emailId).get();
    }
    @Override
    public boolean isCached(String emailId){
        if(emailRepository.findById(emailId).isPresent()){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public CachedEmailsResponse getCachedEmailsById(int pageNumber) throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();
        Pageable pageable = PageRequest.of(pageNumber,10, Sort.by("createdAt").descending());
        Page<Email> page =  emailRepository.findByUserId(user.getId(),pageable);
        List<Email> emails =  page.getContent();
        List<EmailOverviewResponse> fEmails = new ArrayList<>();
        for (Email email:emails){
            EmailOverviewResponse response = new EmailOverviewResponse(email.getEmailId(),email.getSubject());
            fEmails.add(response);
        }
        return new  CachedEmailsResponse(fEmails);
    }
}
