package com.shakir.email_summarizer.Response;

import java.util.List;

public class EmailsFetchResponse {

    private String paginationToken;
    private List<EmailOverviewResponse> emails;


    public EmailsFetchResponse(String paginationToken, List<EmailOverviewResponse> emails) {
        this.paginationToken = paginationToken;
        this.emails = emails;
    }

    public String getPaginationToken() {
        return paginationToken;
    }

    public void setPaginationToken(String paginationToken) {
        this.paginationToken = paginationToken;
    }

    public List<EmailOverviewResponse> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailOverviewResponse> emails) {
        this.emails = emails;
    }
}
