package com.auth0.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * Holds the default configuration for the library
 * Taken from properties files
 */
@Component
@Configuration
@PropertySource("classpath:auth0.properties")
public class Auth0Config {

    @Value(value = "${auth0.clientId}")
    private String clientId;

    @Value(value = "${auth0.clientSecret}")
    private String clientSecret;

    @Value(value = "${auth0.domain}")
    private String domain;

    @Value(value = "${auth0.onLogoutRedirectTo}")
    private String onLogoutRedirectTo;

    @Value(value = "${auth0.loginRedirectOnSuccess}")
    private String loginRedirectOnSuccess;

    @Value(value = "${auth0.loginRedirectOnFail}")
    private String loginRedirectOnFail;

    @Value(value = "${auth0.loginCallback}")
    private String loginCallback;


    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getDomain() {
        return domain;
    }

    public String getOnLogoutRedirectTo() {
        return onLogoutRedirectTo;
    }

    public String getLoginRedirectOnSuccess() {
        return loginRedirectOnSuccess;
    }

    public String getLoginRedirectOnFail() {
        return loginRedirectOnFail;
    }

    public String getLoginCallback() {
        return loginCallback;
    }

}

