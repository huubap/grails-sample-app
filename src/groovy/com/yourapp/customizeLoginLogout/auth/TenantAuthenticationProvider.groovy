//package com.yourapp.customizeLoginLogout.auth
//
//import com.yourapp.Tenant
//import com.yourapp.User
//import com.yourapp.customizeLoginLogout.TenantAuthentication
//import grails.plugin.springsecurity.userdetails.GormUserDetailsService
//import grails.plugin.springsecurity.userdetails.GrailsUser
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.security.authentication.AuthenticationProvider
//import org.springframework.security.authentication.BadCredentialsException
//import org.springframework.security.authentication.dao.SaltSource
//import org.springframework.security.authentication.encoding.PasswordEncoder
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.AuthenticationException
//import org.springframework.security.core.authority.GrantedAuthorityImpl
//import org.springframework.security.core.userdetails.UserDetailsChecker
//import org.springframework.security.core.userdetails.UsernameNotFoundException
//
//class TenantAuthenticationProvider implements AuthenticationProvider {
//
//  protected final Logger log = LoggerFactory.getLogger(getClass())
//
//  PasswordEncoder passwordEncoder
//  SaltSource saltSource
//  UserDetailsChecker preAuthenticationChecks
//  UserDetailsChecker postAuthenticationChecks
//
//  Authentication authenticate(Authentication auth) throws AuthenticationException {
//    TenantAuthentication authentication = auth
//
//    String password = authentication.credentials
//    String username = authentication.name
//    String tenantCode = authentication.tenantCode
//    Long tenantId = authentication.tenantId
//    Tenant tenant = Tenant.get(tenantId)
//
//    GrailsUser userDetails
//    def authorities
//
//    // use withTransaction to avoid lazy loading exceptions
//    User.withTransaction { status ->
//      if (!tenant) {
//        // TODO customize 'springSecurity.errors.login.fail' i18n message in app's messages.properties with org name
//        log.warn "Tenant not found: $tenantCode"
//        throw new UsernameNotFoundException('tenant not found', tenantCode)
//      }
//      def user = User.findByUsernameAndTenant(username, tenant)
//      if (!user) {
//        user = User.findByAltUsernameAndTenant(username, tenant)
//      }
//
//      if (!user || (user.tenant.id != tenant.id)) {
//        // TODO customize 'springSecurity.errors.login.fail' i18n message in app's messages.properties with org name
//        log.warn "User not found: $username in tenant $tenantCode"
//        throw new UsernameNotFoundException('User not found', username)
//      }
//
//      authorities = user.authorities.collect { new GrantedAuthorityImpl(it.authority) }
//      authorities = authorities ?: GormUserDetailsService.NO_ROLE
//
//      userDetails = new GrailsUser(user.username, user.password,
//        user.enabled, !user.accountExpired, !user.passwordExpired,
//        !user.accountLocked, authorities, user.id)
//    }
//
//    preAuthenticationChecks.check userDetails
//    additionalAuthenticationChecks userDetails, authentication
//    postAuthenticationChecks.check userDetails
//
//    def result = new TenantAuthentication(userDetails, authentication.credentials, tenantCode, tenantId, authorities)
//    result.details = authentication.details
//    result
//  }
//
//  protected void additionalAuthenticationChecks(GrailsUser userDetails,
//                                                TenantAuthentication authentication) throws AuthenticationException {
//
//    def salt = saltSource.getSalt(userDetails)
//
//    if (authentication.credentials == null) {
//      log.debug 'Authentication failed: no credentials provided'
//      throw new BadCredentialsException('Bad credentials', userDetails)
//    }
//
//    String presentedPassword = authentication.credentials
//    if (!passwordEncoder.isPasswordValid(userDetails.password, presentedPassword, salt)) {
//      log.debug 'Authentication failed: password does not match stored value'
//
//      throw new BadCredentialsException('Bad credentials', userDetails)
//    }
//  }
//
//  boolean supports(Class<? extends Object> authenticationClass) {
//    TenantAuthentication.isAssignableFrom authenticationClass
//  }
//}

