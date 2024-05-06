package com.github.easyhttp.test;

import com.github.easyhttp.core.builder.HttpGetRequestBuilder;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/28 0:13
 */
public class GetRequestTest {


    public static void main(String[] args) {

        String s = new HttpGetRequestBuilder().uri("http://localhost:8080/easy-http/getTest").executeAsString();

        System.out.println(s);


    }


}
