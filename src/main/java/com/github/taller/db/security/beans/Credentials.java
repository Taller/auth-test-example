package com.github.taller.db.security.beans;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 01.03.14
 */
public class Credentials {
    private String auth;
    private String key;

    public Credentials(String auth, String key) {
        this.auth = auth;
        this.key = key;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Credentials)) {
            return false;
        }

        Credentials that = (Credentials) o;

        if (this.auth == null || this.key == null) {
            return false;
        }

        if (this.auth.equals(that.auth) && this.key.equals(that.key)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = (auth != null ? auth.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }
}
