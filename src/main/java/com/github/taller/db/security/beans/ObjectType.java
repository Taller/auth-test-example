package com.github.taller.db.security.beans;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 01.03.14
 */
public enum ObjectType {
    TABLE("TABLE"),
    VIEW("VIEW"),
    FUNCTION("FUNCTION");

    private String code;

    private ObjectType(String code) {
        this.code = code;
    }

    public static ObjectType getObjectTypeByCode(String code) {

        if (code == null || code.isEmpty()) {
            return null;
        }

        for (ObjectType ot : values()) {
            if (ot.code.equals(code.toUpperCase())) {
                return ot;
            }
        }

        return null;
    }


    @Override
    public String toString() {
        return  code;
    }
}
