package com.auth0.spring.security.mvc;

import com.auth0.authentication.result.UserIdentity;
import com.auth0.web.Auth0User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Spring Security UserDetails Object
 * Spring representation of Auth0 UserProfile Object
 */
public class Auth0UserDetails implements UserDetails {

    private static final long serialVersionUID = 2058797193125711681L;

    private static final Logger logger = LoggerFactory.getLogger(Auth0UserDetails.class);

    private String userId;
    private String username;
    private String name;
    private String email;
    private boolean emailVerified;
    private String nickname;
    private String picture;
    private Map<String, Object> extraInfo;
    private List<UserIdentity> identities;
    private ArrayList<GrantedAuthority> authorities;

    public Auth0UserDetails(final Auth0User auth0User, final Auth0AuthorityStrategy authorityStrategy) {
        this.userId = auth0User.getUserId();
        if (auth0User.getEmail() != null) {
            this.username = auth0User.getEmail();
        } else if (auth0User.getUserId() != null) {
            this.username = auth0User.getUserId();
        } else {
            this.username = "UNKNOWN_USER";
        }
        this.name = auth0User.getName();
        this.email = auth0User.getEmail();
        if (email != null) {
            emailVerified = auth0User.isEmailVerified();
        }
        this.nickname = auth0User.getNickname();
        this.picture = auth0User.getPicture();
        this.identities = auth0User.getIdentities();
        this.extraInfo = auth0User.getExtraInfo();
//        setupAuthorities(auth0User);

        setupGrantedAuthorities(auth0User, authorityStrategy);
    }


    /**
     * Currently support Groups and Roles only...
     */
    private void setupGrantedAuthorities(final Auth0User auth0User, final Auth0AuthorityStrategy authorityStrategy) {
        this.authorities = new ArrayList<GrantedAuthority>();
        if (Auth0AuthorityStrategy.ROLES.equals(authorityStrategy)) {
            if (auth0User.getRoles() != null) {
                logger.debug("Attempting to map Roles");
                try {
                    for (final String role : auth0User.getRoles()) {
                        this.authorities.add(new SimpleGrantedAuthority(role));
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    logger.error("Error setting up GrantedAuthority using Roles");
                }
            }
        } else if (Auth0AuthorityStrategy.GROUPS.equals(authorityStrategy)) {
            if (auth0User.getGroups() != null) {
                logger.debug("Attempting to map Groups");
                try {
                    for (final String group : auth0User.getGroups()) {
                        this.authorities.add(new SimpleGrantedAuthority(group));
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    logger.error("Error setting up GrantedAuthority using Groups");
                }
            }
        } else if (Auth0AuthorityStrategy.SCOPE.equals(authorityStrategy)) {
            throw new IllegalStateException("SCOPE authority strategy currently not supported for MVC apps");
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPicture() {
        return picture;
    }

    public List<UserIdentity> getIdentities() {
        return identities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Will return UnsupportedOperationException
     */
    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("Password is protected");
    }

    /**
     * Gets the email if it exists otherwise it returns the user_id
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        // @TODO - review this
        return false;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        // @TODO - review this
        return false;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // @TODO - review this
        return false;
    }

    /**
     * Will return true if the email is verified, otherwise it will return false
     */
   @Override
    public boolean isEnabled() {
        return emailVerified;
    }


}
