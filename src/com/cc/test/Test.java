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
        List<Ship> listship = new ArrayList<Ship>();
        Ship test_ship=new Ship(1,"TEST331","油船","深圳","ZC","内河",46.5,8.6,3.6,385,503,"8140ZC450-1",660, "8/20/2015","台州市路桥金清海祥船舶修造有限公司","8/20/2016",0);
        Ship test2_ship=new Ship(1,"TEST331","油船","深圳","ZC","内河",46.5,8.6,3.6,385,503,"8140ZC450-1",660, "8/20/2015","台州市路桥金清海祥船舶修造有限公司","8/20/2016",0);
        listship.add(test_ship);
        listship.add(test2_ship);
        Gson gson = new Gson();
        PostData p = new PostData();
        p.setToken("donghui");
        p.setData(listship);
        System.out.println(gson.toJson(p));
    }

}
