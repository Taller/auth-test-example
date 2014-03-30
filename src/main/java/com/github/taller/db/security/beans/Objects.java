package com.github.taller.db.security.beans;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 01.03.14
 */
public class Objects {
    private String name;
    private ObjectType type;

    public Objects(String name, ObjectType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Objects)) {
            return false;
        }

        Objects that = (Objects) o;

        if (this.name == null || this.type == null) {
            return false;
        }

        if (this.name.equals(that.name) && this.type.equals(that.type)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
