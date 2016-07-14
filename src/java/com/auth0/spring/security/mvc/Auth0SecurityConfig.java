package com.auth0.spring.security.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.context.request.RequestContextListener;

/**
 * Auth0 Security Config that wires together dependencies required
 * <p>
 * Applications are expected to extend this Config
 */
@Configuration
@EnableWebSecurity(debug = true)
@PropertySource("classpath:auth0.properties")
public class Auth0SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${auth0.domain}")
    protected String domain;

    @Value(value = "${auth0.issuer}")
    protected String issuer;

    @Value(value = "${auth0.clientId}")
    protected String clientId;

    @Value(value = "${auth0.clientSecret}")
    protected String clientSecret;

    @Value(value = "${auth0.securedRoute}")
    protected String securedRoute;

    @Value(value = "${auth0.authorityStrategy}")
    protected String authorityStrategy;

    @Value(value = "${auth0.base64EncodedSecret}")
    protected boolean base64EncodedSecret;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean(name = "auth0AuthenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "auth0CORSFilter")
    public Auth0CORSFilter simpleCORSFilter() {
        return new Auth0CORSFilter();
    }

    @Bean(name = "auth0AuthenticationProvider")
    public Auth0AuthenticationProvider auth0AuthenticationProvider() {
        // First check the authority strategy configured for the API
        if (!Auth0AuthorityStrategy.contains(this.authorityStrategy)) {
            throw new IllegalStateException("Configuration error, illegal authority strategy");
        }
        final Auth0AuthorityStrategy authorityStrategy = Auth0AuthorityStrategy.valueOf(this.authorityStrategy);
        if (Auth0AuthorityStrategy.SCOPE.equals(authorityStrategy)) {
            throw new IllegalStateException("SCOPE authority strategy currently not supported for MVC apps");
        }
        final Auth0AuthenticationProvider authenticationProvider = new Auth0AuthenticationProvider();
        authenticationProvider.setDomain(domain);
        authenticationProvider.setIssuer(issuer);
        authenticationProvider.setClientId(clientId);
        authenticationProvider.setClientSecret(clientSecret);
        authenticationProvider.setSecuredRoute(securedRoute);
        authenticationProvider.setAuthorityStrategy(authorityStrategy);
        authenticationProvider.setBase64EncodedSecret(base64EncodedSecret);
        return authenticationProvider;
    }

    @Bean(name = "auth0EntryPoint")
    public Auth0AuthenticationEntryPoint auth0AuthenticationEntryPoint() {
        return new Auth0AuthenticationEntryPoint();
    }

    @Bean(name = "auth0Filter")
    public Auth0AuthenticationFilter auth0AuthenticationFilter(Auth0AuthenticationEntryPoint entryPoint) {
        final Auth0AuthenticationFilter filter = new Auth0AuthenticationFilter();
        filter.setEntryPoint(entryPoint);
        return filter;
    }

    /**
     * We do this to ensure our Filter is only loaded once into Application Context
     * <p>
     * If using Spring Boot, any GenericFilterBean in the context will be automatically added to the filter chain.
     */
    @Bean(name = "auth0AuthenticationFilterRegistration")
    public FilterRegistrationBean auth0AuthenticationFilterRegistration(final Auth0AuthenticationFilter filter) {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(auth0AuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        // Disable CSRF for JWT usage
        http.csrf().disable();

        // Add Auth0 Authentication Filter
        http.addFilterAfter(auth0AuthenticationFilter(auth0AuthenticationEntryPoint()), SecurityContextPersistenceFilter.class);
        http.addFilterBefore(simpleCORSFilter(), Auth0AuthenticationFilter.class);

        // Apply the Authentication and Authorization Strategies your application endpoints require
        authorizeRequests(http);

        // Auth0 library will will control session management explicitly - not spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    /**
     * Lightweight default configuration that offers basic authorization checks for authenticated
     * users on secured endpoint, and sets up a Principal user object with granted authorities
     *
     *  Update for your application's needs
     */
    protected void authorizeRequests(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/fonts/**", "/assets/**", "/login").permitAll()
                .antMatchers("/portal/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers(securedRoute).authenticated();
    }

}