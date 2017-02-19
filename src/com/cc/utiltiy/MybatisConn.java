package com.cc.utiltiy;




import com.cc.bean.Ship;
import com.google.gson.Gson;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;
import java.util.List;

/**
 * Created by cc on 2017/2/16.
 */
public class MybatisConn {

    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    static {
        try {

            reader = Resources.getResourceAsReader("com/cc/config/mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static SqlSessionFactory getSession() {
        return sqlSessionFactory;
    }

    public static void addShip(List<Ship> shipList) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            if(shipList!=null){
                for (Ship ship:shipList
                     ) {
                    session.insert(
                            "com.cc.Mapping.ShipMapper.insertShip",ship);
                    session.commit();
                }
            }
        } finally {
            session.close();
        }
    }
}
