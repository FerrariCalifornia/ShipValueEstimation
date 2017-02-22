package com.cc.utiltiy;

import com.cc.algorithm.Algorithm;
import com.cc.bean.PostData;
import com.cc.bean.Result;
import com.cc.bean.Ship;
import com.google.gson.Gson;
import org.springframework.validation.BindingResult;

import java.beans.beancontext.BeanContextMembershipListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.cc.utiltiy.MybatisConn.addShip;

/**
 * Created by cc on 2017/2/19.
 */
public class Function {
    public String responseMessage(PostData postData){
        Result result = new Result();
        String message=verify(postData.getToken());
        if(message.equals("success")){
            //验证成功进行预测
            Algorithm algorithm = new Algorithm();
            List<Ship> data=algorithm.predict(postData.getData());
            //将预测结果和数据存入数据库。
            addShip(data);
            result.setMessage(message);
            result.setData(data);
        }else {
            result.setMessage(message);
        }
            Gson gson = new Gson();
        return gson.toJson(result);
    }



    //验证是否过期
    //verify the token whether expired or not
    public  String verify(String token){
        MybatisConn mybatisConn = new MybatisConn();
        //find the expiration_date through the token which is get from the post request
        Date expiration_date = mybatisConn.findToken(token);
        if(expiration_date==null){
            return "Please contact your administrator!";
        }
        return dataCompare(expiration_date);
    }

    //compare the date
    public static String dataCompare(Date expiration_date){
        //获取当前时间
        java.util.Date nowdate=new java.util.Date();
        //给定一个到期时间
        String outDate = new SimpleDateFormat("yyyy-MM-dd").format(expiration_date);
        //设置要转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Date d;
        try {
            d = sdf.parse(outDate);
            boolean flag = d.before(nowdate);
            if(flag)
            {
                //今天的日期大于比给定的到期时间(outDate)(即今天的日期在到期日期之后，也就是当前时间已到期)
//                System.out.print("已到期");
                return "使用权限已到期";
            }

            else
            {
                //今天的日期小于比给定的到期时间(outDate)(即今天的日期在到期日期之前，也就是当前时间还未到期)
//                System.out.print("未到期");
                return "success";
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    //request field control
    public void fieldControl(List<Ship> shipList){
        for (Ship ship :
                shipList) {
            ship.getHeight();
        }
    }

    public  String validate(BindingResult result){
        if(result.hasErrors()){
          int errornumber=  result.getErrorCount();
            return "error:"+String.valueOf(errornumber);
        }else {
            return "success";
        }
    }
}
