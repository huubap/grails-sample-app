// Place your Spring DSL code here
import com.yourapp.customizeLoginLogout.auth.TenantAuthenticationProvider
import com.yourapp.customizeLoginLogout.ui.TenantFilter
import grails.plugin.springsecurity.SpringSecurityUtils

beans = {
  def conf = SpringSecurityUtils.securityConfig

  // custom authentication
  authenticationProcessingFilter(TenantFilter) {
    authenticationManager = ref('authenticationManager')
    sessionAuthenticationStrategy = ref('sessionAuthenticationStrategy')
    authenticationSuccessHandler = ref('authenticationSuccessHandler')
    authenticationFailureHandler = ref('authenticationFailureHandler')
    rememberMeServices = ref('rememberMeServices')
    authenticationDetailsSource = ref('authenticationDetailsSource')
    filterProcessesUrl = conf.apf.filterProcessesUrl
    usernameParameter = conf.apf.usernameParameter
    passwordParameter = conf.apf.passwordParameter
    continueChainBeforeSuccessfulAuthentication = conf.apf.continueChainBeforeSuccessfulAuthentication
    allowSessionCreation = conf.apf.allowSessionCreation
    postOnly = conf.apf.postOnly
    storeLastUsername = conf.apf.storeLastUsername // false
  }

  // custom authentication
  daoAuthenticationProvider(TenantAuthenticationProvider) {
    passwordEncoder = ref('passwordEncoder')
    saltSource = ref('saltSource')
    preAuthenticationChecks = ref('preAuthenticationChecks')
    postAuthenticationChecks = ref('postAuthenticationChecks')
  }
}
