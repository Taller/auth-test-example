package com.github.taller.db.security.beans;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 01.03.14
 */
public class Subjects {
    private SubjectType type;
    private Credentials credentials;
    private long subjId;

    public Subjects(Credentials credentials, long subjId, SubjectType type) {
        this.credentials = credentials;
        this.subjId = subjId;
        this.type = type;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public long getSubjId() {
        return subjId;
    }

    public void setSubjId(long subjId) {
        this.subjId = subjId;
    }

    public SubjectType getType() {
        return type;
    }

    public void setType(SubjectType type) {
        this.type = type;
    }
}
