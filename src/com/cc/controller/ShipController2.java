package com.cc.controller;

import com.cc.bean.PostData;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * Created by cc on 2017/4/6.
 */

@Controller
public class ShipController2 {

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/ship90", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody()
    public void test1(@RequestBody PostData postData, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        System.out.println("hehe sb spring");
        PrintWriter pw = response.getWriter();
        Gson gson = new Gson();
        String json = gson.toJson(postData);
        System.out.println(json);
        String url = null;
        Properties prop = new Properties();
        try {
            prop.load(ShipController.class.getClassLoader().getResourceAsStream("file.properties"));
            url = prop.getProperty("url90");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String respContent = null;
        CloseableHttpClient client = HttpClients.createDefault();

        // 创建httppost
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");

        }
        pw.print(respContent);
        pw.close();
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/ship20", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody()
    public void test2(@RequestBody PostData postData, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        Gson gson = new Gson();
        String json = gson.toJson(postData);

        System.out.println(json);
        String url = null;
        Properties prop = new Properties();
        try {
            prop.load(ShipController.class.getClassLoader().getResourceAsStream("file.properties"));
            url = prop.getProperty("url20");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String respContent = null;
        CloseableHttpClient client = HttpClients.createDefault();

        // 创建httppost
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        PrintWriter pw = response.getWriter();
        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");
        }
        pw.print(respContent);
        pw.close();
    }


}
