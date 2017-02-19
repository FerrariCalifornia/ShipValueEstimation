package com.cc.bean;

import java.util.Date;


/**
 * Created by cc on 2017/2/19.
 */
public class Authority {
    private int id;
    private String token;
    private Date expiration_date;
    private int privilege;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public Authority() {
    }

    public Authority(int id, String token, Date expriation_date, int privilege) {
        this.id = id;
        this.token = token;
        this.expiration_date = expriation_date;
        this.privilege = privilege;
    }
}
