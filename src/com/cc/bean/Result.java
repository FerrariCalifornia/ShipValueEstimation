package com.cc.bean;

import java.util.List;

/**
 * Created by cc on 2017/2/19.
 */
public class Result {
    private List<Ship> data;
    private String message;

    public Result() {
    }

    public Result(List<Ship> data, String message) {
        this.data = data;
        this.message = message;
    }

    public List<Ship> getData() {
        return data;
    }

    public void setData(List<Ship> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
