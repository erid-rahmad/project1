package com.rid.springjwt.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


public class TransactionDTO  {

    private long id;
    private String name;
    private Integer poin;
    private Integer Nominal;
    private Date date;
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



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public com.rid.springjwt.models.User getUser() {
        return User;
    }

    public void setUser(com.rid.springjwt.models.User user) {
        User = user;
    }

    public Integer getPoin() {
        return poin;
    }

    public void setPoin(Integer poin) {
        this.poin = poin;
    }

    public Integer getNominal() {
        return Nominal;
    }

    public void setNominal(Integer nominal) {
        Nominal = nominal;
    }
}
