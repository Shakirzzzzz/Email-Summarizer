package com.shakir.email_summarizer.Entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
public class Email {
    @Id
    @Column(nullable = false)
    private String emailId;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false,columnDefinition = "LONGTEXT")
    private String messageBody;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    @CreationTimestamp
    @Column(updatable = false,name = "created_at")
    private Date createdAt;


    public Email(){}

    public Email(String emailId,String subject, String messageBody) {
        this.emailId = emailId;
        this.messageBody = messageBody;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Email{" +
                "emailId='" + emailId + '\'' +
                ", messageBody='" + messageBody + '\'' +
                '}';
    }
}
