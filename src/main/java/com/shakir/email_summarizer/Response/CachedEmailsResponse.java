package com.shakir.email_summarizer.Response;

import java.util.List;

public class CachedEmailsResponse {

    private List<EmailOverviewResponse> emails;

    public CachedEmailsResponse(List<EmailOverviewResponse> emails) {
        this.emails = emails;
    }

    public List<EmailOverviewResponse> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailOverviewResponse> emails) {
        this.emails = emails;
    }
}
