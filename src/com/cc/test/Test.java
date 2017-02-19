package com.cc.test;

import com.cc.bean.PostData;
import com.cc.bean.Ship;
import com.cc.utiltiy.MybatisConn;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by cc on 2017/2/17.
 */
public class Test {
    public static void main(String[] args){
        List<Ship> ships = new ArrayList<Ship>();
        Ship ship = new Ship();
        ship.setDistrict("xian");
        ship.setIndex("FF4");
        ship.setEnginepower(2.22);
        ships.add(ship);
        Gson gson = new Gson();
        PostData p = new PostData();
        p.setToken("chenchun");
        p.setData(ships);
        System.out.println(gson.toJson(p));

    }

}
