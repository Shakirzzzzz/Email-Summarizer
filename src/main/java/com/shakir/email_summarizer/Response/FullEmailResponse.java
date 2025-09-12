package com.shakir.email_summarizer.Response;

public class FullEmailResponse {
    private String emailId;
    private String Subject;
    private String body;

    public FullEmailResponse(String emailId, String subject, String body) {
        this.emailId = emailId;
        Subject = subject;
        this.body = body;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
