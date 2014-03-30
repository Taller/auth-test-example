package com.github.taller.db.security.beans;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 01.03.14
 */
public enum SubjectType {
    USER("USER"),
    GROUP("GROUP");

    private String code;

    private SubjectType(String code) {
        this.code = code;
    }

    public SubjectType getSubjectTypeByCode(String code) {
        if (code == null || code.isEmpty()) {
            return  null;
        }


        for (SubjectType st : values()) {
            if (st.code.equals(code.toUpperCase())) {
                return st;
            }
        }

        return null;
    }


    @Override
    public String toString() {
        return code;
    }
}
