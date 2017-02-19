package com.cc.utiltiy;


import com.cc.bean.Authority;
import com.cc.bean.Ship;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;
import java.util.Date;
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
            if (shipList != null) {
                for (Ship ship : shipList
                        ) {
                    session.insert(
                            "com.cc.Mapping.ShipMapper.insertShip", ship);
                    session.commit();
                }
            }
        } finally {
            session.close();
        }
    }

    public Date findToken(String token) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            if (token != null) {
                Authority authority = session.selectOne(
                        "com.cc.Mapping.AuthorityMapper.selectAuthority", token);
//                System.out.println(authority.getId());
//                System.out.println(authority.getExpiration_date());

                if (authority != null) {
                    return authority.getExpiration_date();
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } finally {
            session.close();
        }
    }
}
