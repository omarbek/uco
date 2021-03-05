package com.company.uco.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "UCO_ORDER")
@Entity(name = "uco_Order")
public class Order extends StandardEntity {
    private static final long serialVersionUID = -5613498596231456206L;

    @NotNull
    @Column(name = "ORDER_DATE", nullable = false)
    private LocalDateTime orderDate;

    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    @Positive
    private Integer amount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @OneToMany(mappedBy = "order")
    private Set<Product> products;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}