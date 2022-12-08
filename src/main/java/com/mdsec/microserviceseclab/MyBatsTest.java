package com.mdsec.microserviceseclab;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.mdsec.microserviceseclab.data.Student;

public class MyBatsTest {

    public static void main(String[] args) throws IOException {
        String resources = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resources);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Student user = sqlSession.selectOne("MyMapper.selectUser", 1);
            System.out.println(user.getUsername());
        }
        finally {

        }
    }

}
