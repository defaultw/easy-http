package com.github.easyhttp.test;

import com.github.easyhttp.core.builder.HttpGetRequestBuilder;
import com.github.easyhttp.test.bo.Person;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/28 0:13
 */
public class GetRequestTest {


    public static void main(String[] args) {

        System.out.println(new HttpGetRequestBuilder().uri("http://localhost:8080/easy-http/getTest")
                .queryParam("id", "1").queryParam("code", "CM_12123123").executeAsString());

        Person person = new HttpGetRequestBuilder().uri("http://localhost:8080/easy-http/getTest")
                .queryParam("id", "1").queryParam("code", "CM_12123123").executeAsObject(Person.class);

        System.out.printf("person id: %d, code: %s%n", person.getId(), person.getCode());

        System.out.println(new HttpGetRequestBuilder().uri("http://localhost:8080/easy-http/getTest/route/${rParam}")
                .routeParam("rParam", "1").executeAsString());

    }


}
