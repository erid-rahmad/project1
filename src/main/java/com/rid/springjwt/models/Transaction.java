package com.rid.springjwt.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    private BigDecimal poin;

    @NotNull
    private BigDecimal Nominal;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private LocalDate date;

    @NotNull
    @ManyToOne(optional = false)
    private User User;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public com.rid.springjwt.models.User getUser() {
        return User;
    }

    public void setUser(com.rid.springjwt.models.User user) {
        User = user;
    }

    public BigDecimal getPoin() {
        return poin;
    }

    public void setPoin(BigDecimal poin) {
        this.poin = poin;
    }

    public BigDecimal getNominal() {
        return Nominal;
    }

    public void setNominal(BigDecimal nominal) {
        Nominal = nominal;
    }
}
