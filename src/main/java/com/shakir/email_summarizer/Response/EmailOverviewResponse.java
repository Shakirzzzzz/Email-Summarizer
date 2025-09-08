package com.shakir.email_summarizer.Response;

public class EmailOverviewResponse {

    private String emailId;
    private String emailSubject;

    public EmailOverviewResponse(String emailId, String emailSubject) {
        this.emailId = emailId;
        this.emailSubject = emailSubject;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
}
