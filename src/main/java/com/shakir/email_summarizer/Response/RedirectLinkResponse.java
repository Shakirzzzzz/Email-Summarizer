package com.shakir.email_summarizer.Response;

public class RedirectLinkResponse {
    private String redirectLink;

    public String getRedirectLink() {
        return redirectLink;
    }

    public RedirectLinkResponse(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }
}
