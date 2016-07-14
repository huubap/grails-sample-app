package com.auth0.spring.security.mvc;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.web.Auth0User;
import com.auth0.web.SessionUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

/**
 * Class that verifies the JWT token and when valid, it will set
 * the userdetails in the authentication object
 */
public class Auth0AuthenticationProvider implements AuthenticationProvider,
        InitializingBean {

    private final Log logger = LogFactory.getLog(getClass());

    private static final AuthenticationException AUTH_ERROR =
            new Auth0TokenException("Authentication Error");

    private JWTVerifier jwtVerifier;
    private String domain;
    private String issuer;
    private String clientId;
    private String clientSecret;
    private String securedRoute;
    private boolean base64EncodedSecret;
    private Auth0AuthorityStrategy authorityStrategy;

    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        try {
            final String token = ((Auth0JWTToken) authentication).getJwt();
            logger.info("Trying to authenticate with token: " + token);
            final Auth0JWTToken tokenAuth = ((Auth0JWTToken) authentication);
            final Map<String, Object> decoded = jwtVerifier.verify(token);
            logger.debug("Decoded JWT token" + decoded);
            tokenAuth.setAuthenticated(true);
            // Retrieve our Auth0User object from session
            final ServletRequestAttributes servletReqAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            final HttpServletRequest req = servletReqAttr.getRequest();
            final Auth0User auth0User = SessionUtils.getAuth0User(req);
            Validate.notNull(auth0User);
            tokenAuth.setPrincipal(new Auth0UserDetails(auth0User, authorityStrategy));
            tokenAuth.setDetails(decoded);
            return authentication;
        } catch (InvalidKeyException e) {
            logger.debug("InvalidKeyException thrown while decoding JWT token "
                    + e.getLocalizedMessage());
            throw AUTH_ERROR;
        } catch (NoSuchAlgorithmException e) {
            logger.debug("NoSuchAlgorithmException thrown while decoding JWT token "
                    + e.getLocalizedMessage());
            throw AUTH_ERROR;
        } catch (IllegalStateException e) {
            logger.debug("IllegalStateException thrown while decoding JWT token "
                    + e.getLocalizedMessage());
            throw AUTH_ERROR;
        } catch (SignatureException e) {
            logger.debug("SignatureException thrown while decoding JWT token "
                    + e.getLocalizedMessage());
            throw AUTH_ERROR;
        } catch (IOException e) {
            logger.debug("IOException thrown while decoding JWT token "
                    + e.getLocalizedMessage());
            throw AUTH_ERROR;
        } catch (JWTVerifyException e) {
            logger.debug("JWTVerifyException thrown while decoding JWT token "
                    + e.getLocalizedMessage());
            throw AUTH_ERROR;
        }
    }

    public boolean supports(Class<?> authentication) {
        return Auth0JWTToken.class.isAssignableFrom(authentication);
    }

    public void afterPropertiesSet() throws Exception {
        if ((clientSecret == null) || (clientId == null)) {
            throw new IllegalStateException(
                    "client secret and client id are not set for Auth0AuthenticationProvider");
        }
        if (securedRoute == null) {
            throw new IllegalStateException(
                    "You must set which route pattern is used to check for users so that they must be authenticated");
        }
        // Auth0 Client Secrets are currently Base64 encoded,
        // Auth0 Resource Server Signing Secrets are not Base64 encoded
        if (base64EncodedSecret) {
            jwtVerifier = new JWTVerifier(new Base64(true).decodeBase64(clientSecret), clientId, issuer);
        } else {
            jwtVerifier = new JWTVerifier(clientSecret, clientId, issuer);
        }
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getSecuredRoute() {
        return securedRoute;
    }

    public void setSecuredRoute(String securedRoute) {
        this.securedRoute = securedRoute;
    }

    public boolean isBase64EncodedSecret() {
        return base64EncodedSecret;
    }

    public void setBase64EncodedSecret(boolean base64EncodedSecret) {
        this.base64EncodedSecret = base64EncodedSecret;
    }

    public Auth0AuthorityStrategy getAuthorityStrategy() {
        return authorityStrategy;
    }

    public void setAuthorityStrategy(Auth0AuthorityStrategy authorityStrategy) {
        this.authorityStrategy = authorityStrategy;
    }
}
