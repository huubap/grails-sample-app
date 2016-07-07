package com.yourapp.customizeLoginLogout

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class TenantAuthentication extends UsernamePasswordAuthenticationToken {

  private static final long serialVersionUID = 1

  final String tenantCode
  final Long tenantId

  TenantAuthentication(principal, credentials, String tenantCode, Long tenantId) {
    super(principal, credentials)
    this.tenantCode = tenantCode
    this.tenantId = tenantId
  }

  TenantAuthentication(principal, credentials, String tenantCode, Long tenantId, Collection authorities) {
    super(principal, credentials, authorities)
    this.tenantCode = tenantCode
    this.tenantId = tenantId
  }
}
