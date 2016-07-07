package com.yourapp

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class LoginController {
  def login() {
    def result = [success: true]
    render result as JSON
  }
}
