package com.cc.bean;

import java.util.List;

/**
 * Created by cc on 2017/2/19.
 */
public class PostData {
    private String token;
    private List<Ship> data;

    public PostData() {
    }

    public PostData(String token, List<Ship> data) {
        this.token = token;
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Ship> getData() {
        return data;
    }

    public void setData(List<Ship> data) {
        this.data = data;
    }
}
