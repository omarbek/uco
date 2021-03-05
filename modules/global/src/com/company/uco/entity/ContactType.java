package com.company.uco.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ContactType implements EnumClass<String> {

    EMAIL("email"),
    PHONE("phone");

    private String id;

    ContactType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ContactType fromId(String id) {
        for (ContactType at : ContactType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}