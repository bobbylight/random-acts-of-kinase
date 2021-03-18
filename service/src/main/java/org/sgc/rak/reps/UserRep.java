package org.sgc.rak.reps;

/**
 * Represents an authenticated user.
 */
public class UserRep {

    private String userName;

    public UserRep(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
