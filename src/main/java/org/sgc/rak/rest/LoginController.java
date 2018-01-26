package org.sgc.rak.rest;

import org.sgc.rak.reps.UserRep;
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
     * user information is returned.  Otherwise, {@code null} is returned.
     *
     * @param user The principal of the logged-in user.
     * @return Information about the logged-in user, or {@code null} if the user isn't logged in.
     */
    @RequestMapping(method = RequestMethod.GET, path = "login")
    public UserRep user(Principal user) {
        if (user == null) {
            return null;
        }
        return new UserRep(user.getName());
    }
}
