package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Entity.Email;
import com.shakir.email_summarizer.Repository.emailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class emailCachingServiceImpl implements emailCachingService{
    private final emailRepository emailRepository;


    @Autowired
    public emailCachingServiceImpl(emailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }


    @Override
    public void storeInCache(String emailId, String subject, String messageBody) {
        Email cachedEmail = new Email(emailId,subject,messageBody);
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
}
