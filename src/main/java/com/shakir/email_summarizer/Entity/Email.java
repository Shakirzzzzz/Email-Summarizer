package com.shakir.email_summarizer.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

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

    @Override
    public String toString() {
        return "Email{" +
                "emailId='" + emailId + '\'' +
                ", messageBody='" + messageBody + '\'' +
                '}';
    }
}
