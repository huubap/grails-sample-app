package com.yourapp

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_USER'])
class ProtectedController {
  def springSecurityService

  def test() {
    def user = springSecurityService.getCurrentUser() as User
    response.setContentType("application/json")
    def result = [message: "You are viewing a protected page", tenant: "${user.tenant.code}", username: "${user.username}", altUsername: "${user.altUsername}"]
    render result as JSON
  }

}
