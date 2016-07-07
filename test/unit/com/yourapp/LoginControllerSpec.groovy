package com.yourapp

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.*

@Mock([User, Tenant, Role, UserRole])
@TestFor(LoginController)
class LoginControllerSpec extends Specification {

  def setup() {
  }

  void "ajax success should include redirect uri with true parameter set in 'firstTimeLogin' when password and username(altusername) are the same"() {
    given:
    def adminRole = new Role(authority: "ROLE_ADMIN").save(flush: true, failOnError: true)
    def userRole = new Role(authority: "ROLE_USER").save(flush: true, failOnError: true)
    Tenant tenant = new Tenant(code:"abc").save(flush: true, failOnError: true)
    User user = new User(username: "admin@abc.com", altUsername: "admin", password: "pass", tenant: tenant).save(flush: true, failOnError: true)
    UserRole.create(user, adminRole, true)

    SpringSecurityUtils.metaClass.'static'.ifAllGranted = { String role -> role == "ROLE_ADMIN" }
    controller.springSecurityService = [ getCurrentUser: { return user } ]

    when:
    controller.ajaxSuccess()

    then:
    response.json as Map == [success: true, authority: "ADMIN"]
  }
}
