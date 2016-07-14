package com.yourapp

import com.auth0.web.Auth0Config
import com.auth0.web.NonceUtils
import com.auth0.web.SessionUtils
import org.springframework.beans.factory.annotation.Autowired

class LoginController {

    static defaultAction = "login"

    @Autowired
    Auth0Config auth0Config

    def login() {
        log.info("Performing login");
        // add a Nonce value to session storage
        NonceUtils.addNonceToStorage(request)

        [clientId: auth0Config.clientId, domain: auth0Config.domain,
         loginCallback: auth0Config.loginCallback, state: SessionUtils.getState(request)]
    }

}
