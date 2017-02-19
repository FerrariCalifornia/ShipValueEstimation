package com.cc.test;

import com.cc.bean.Ship;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cc on 2017/2/17.
 */
public class Test {
    public static void main(String[] args){
        List<Ship> shipList = new ArrayList<Ship>();
        Ship ship = new Ship();
        ship.setIndex("DB3");
        ship.setDistrict("California");
        shipList.add(ship);
        shipList.add(ship);
        Gson gson = new Gson();
        System.out.println(gson.toJson(shipList));
    }
}
