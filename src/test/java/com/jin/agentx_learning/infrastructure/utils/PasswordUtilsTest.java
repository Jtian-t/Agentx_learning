package com.jin.agentx_learning.infrastructure.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class PasswordUtilsTest {


    @Test
    //利用PasswordUtils生成密码的测试-主要是为了生成一个密码
    public  void generatePass(){
        String pass="admin";
        String encodedPassword = PasswordUtils.encode(pass);
        System.out.println(encodedPassword);

    }

}
