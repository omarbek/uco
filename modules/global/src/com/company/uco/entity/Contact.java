package com.company.uco.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@NamePattern("%s (%s)|value,contactType")
@Table(name = "UCO_CONTACTS")
@Entity(name = "uco_Contacts")
public class Contact extends StandardEntity {
    private static final long serialVersionUID = 521183893318286732L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACCOUNT_ID")
    @NotNull
    private Account account;

    @NotNull
    @Column(name = "CONTACT_TYPE", nullable = false)
    private String contactType;

    @NotNull
    @Column(name = "VALUE_", nullable = false)
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContactType getContactType() {
        return contactType == null ? null : ContactType.fromId(contactType);
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType == null ? null : contactType.getId();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Contact contact = (Contact) o;
        return Objects.equals(account, contact.account) && Objects.equals(contactType, contact.contactType) && Objects.equals(value, contact.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), account, contactType, value);
    }

}