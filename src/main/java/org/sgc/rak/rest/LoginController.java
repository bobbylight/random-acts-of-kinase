package org.sgc.rak.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * REST API for logging in and out.
 */
@RestController
@RequestMapping(path = "/")
public class LoginController {

    /**
     * Login endpoint.  If the current user authenticated (via basic auth), their
     * user information is returned.  If they failed to authenticate, Spring returns
     * a 401 Unauthorized.
     *
     * @param user The principal of the logged-in user.
     * @return The logged-in user's principal.
     */
    @RequestMapping(method = RequestMethod.POST, path = "login")
    public Principal user(Principal user) {
        return user;
    }
}
