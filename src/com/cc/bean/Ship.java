package com.cc.bean;

import com.sun.istack.internal.NotNull;

/**
 * Created by cc on 2017/2/16.
 */
public class Ship {
    private int id;
    private String index;
    private String type;
    private String port;
    private String level;
    private String district;
    private double length;
    private double width;
    private double height;
    private double grosston;
    private double deadweight;
    private String enginetype;
    private double enginepower;
    private String builddate;
    private String factory;
    private String dealdate;
    private double predict;

    public Ship() {
    }



    public Ship(int id, String index, String type, String port,
                String level, String district, double length,
                double width, double height, double grosston,
                double deadweight, String enginetype, double enginepower
            , String builddate, String factory, String dealdate, double predict) {
        this.id = id;
        this.index = index;
        this.type = type;
        this.port = port;
        this.level = level;
        this.district = district;
        this.length = length;
        this.builddate = builddate;
        this.dealdate = dealdate;
        this.width = width;
        this.height = height;
        this.grosston = grosston;
        this.deadweight = deadweight;
        this.enginetype = enginetype;
        this.enginepower = enginepower;
        this.factory = factory;
        this.predict = predict;

    }

    public double getPredict() {
        return predict;
    }

    public void setPredict(double predict) {
        this.predict = predict;
    }

    public String getBuilddate() {
        return builddate;
    }

    public void setBuilddate(String builddate) {
        this.builddate = builddate;
    }

    public String getDealdate() {
        return dealdate;
    }

    public void setDealdate(String dealdate) {
        this.dealdate = dealdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getGrosston() {
        return grosston;
    }

    public void setGrosston(double grosston) {
        this.grosston = grosston;
    }

    public double getDeadweight() {
        return deadweight;
    }

    public void setDeadweight(double deadweight) {
        this.deadweight = deadweight;
    }

    public String getEnginetype() {
        return enginetype;
    }

    public void setEnginetype(String enginetype) {
        this.enginetype = enginetype;
    }

    public double getEnginepower() {
        return enginepower;
    }

    public void setEnginepower(double enginepower) {
        this.enginepower = enginepower;
    }


    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }


}
