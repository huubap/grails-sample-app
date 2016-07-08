package com.yourapp

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_USER'])
class ProtectedController {
  def springSecurityService

  def test() {
    def user = springSecurityService.getCurrentUser() as User
    response.setContentType("application/json")
    def users = User.list()
    def result = [
      message: "You are viewing a protected page",
      tenant: "${user.tenant.code}",
      username: "${user.username}",
      altUsername: "${user.altUsername}",
      users: users
    ]
    render result as JSON
  }
}
