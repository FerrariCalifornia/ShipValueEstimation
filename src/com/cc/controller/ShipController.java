package com.cc.controller;

import com.cc.algorithm.Algorithm;
import com.cc.bean.PostData;
import com.cc.bean.Ship;
import com.cc.utiltiy.Function;
import com.cc.utiltiy.MybatisConn;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by cc on 2017/2/16.
 */
@Controller
public class ShipController {

    @RequestMapping(value = "/ship2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody()
    public void test2(@RequestBody List<Ship> shipList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        Gson gson2 = new Gson();
        System.out.println("get parm:" + gson2.toJson(shipList));
        Algorithm algorithm = new Algorithm();
        List<Ship> shipListWithResult = algorithm.predict(shipList);
        PrintWriter pw = response.getWriter();
        Gson gson = new Gson();
        String json = gson.toJson(shipListWithResult);
        pw.print(json.toString());
        pw.close();
        MybatisConn mybatisConn = new MybatisConn();
        mybatisConn.addShip(shipListWithResult);
    }

    @RequestMapping(value = "/ship", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody()
    public void test(@RequestBody PostData postData, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        PrintWriter pw = response.getWriter();
        Function function = new Function();
        pw.print(function.responseMessage(postData));
        pw.close();
    }
}
