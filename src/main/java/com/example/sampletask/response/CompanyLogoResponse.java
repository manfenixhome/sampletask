package com.example.sampletask.response;

public class CompanyLogoResponse {
    private String url;

    public CompanyLogoResponse() { }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "CompanyLogoResponse{" +
                "url='" + url + '\'' +
                '}';
    }
}
