package com.yourapp

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class LoginController {
  def login() {
    def result = [success: true]
    render result as JSON
  }

  /**
   * The Ajax success redirect url.
   */
  def ajaxSuccess() {
    System.println("Ajax sign-in succeed!")
    render([success: true] as JSON)
  }
}
