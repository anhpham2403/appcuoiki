/**
 * Created by Philip A Senger on November 10, 2015
 */
package com.anh.movie.entities;

import java.io.Serializable;

public class Token implements Serializable {
    private static final long serialVersionUID = -186954891348069462L;
    private String authToken;

    public Token() { // for some reason the jackson engine needs a zero arg constructor.
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
