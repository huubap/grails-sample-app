package com.yourapp

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class LoginController {
  def springSecurityService

  /**
   * The Ajax success redirect url.
   */
  def ajaxSuccess() {
    println("Ajax sign-in succeed!")

    User user = springSecurityService.getCurrentUser() as User
    String authority = ""

    if (SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')) {
      println("Admin (${user.username}) has signed in")
      authority = "ADMIN"
    } else if (SpringSecurityUtils.ifAllGranted('ROLE_USER')) {
      println("General user (${user.username}) has signed in")
      authority = "USER"
    }
    render([success: true, authority: authority] as JSON)
  }
}
