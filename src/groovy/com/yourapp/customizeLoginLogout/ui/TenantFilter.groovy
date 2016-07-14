//package com.yourapp.customizeLoginLogout.ui
//
//import com.yourapp.Tenant
//import grails.plugin.springsecurity.web.authentication.RequestHolderAuthenticationFilter
//import com.yourapp.customizeLoginLogout.TenantAuthentication
//import org.springframework.security.authentication.AuthenticationServiceException
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.AuthenticationException
//import org.springframework.security.web.util.TextEscapeUtils
//
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//import javax.servlet.http.HttpSession
//
//class TenantFilter extends RequestHolderAuthenticationFilter {
//
//  @Override
//  Authentication attemptAuthentication(HttpServletRequest request,
//                                       HttpServletResponse response)
//    throws AuthenticationException {
//
//    if (!request.post) {
//      throw new AuthenticationServiceException(
//        "Authentication method not supported: $request.method")
//    }
//
//    String username = (obtainUsername(request) ?: '').trim()
//    String password = obtainPassword(request) ?: ''
//    String tenantCode = request.getParameter('j_tenantCode')
//    Tenant tenant = Tenant.findByCode(tenantCode)
//    Long tenantId = tenant?.id ?: -1
//
//    def authentication = new TenantAuthentication(username, password, tenantCode, tenantId)
//
//    HttpSession session = request.getSession(false)
//    if (session || getAllowSessionCreation()) {
//      request.session[SPRING_SECURITY_LAST_USERNAME_KEY] =
//        TextEscapeUtils.escapeEntities(username)
//    }
//
//    setDetails request, authentication
//
//    return getAuthenticationManager().authenticate(authentication)
//  }
//}
