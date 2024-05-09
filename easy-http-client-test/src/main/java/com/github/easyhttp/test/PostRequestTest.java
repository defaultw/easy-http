package com.github.easyhttp.test;

import com.github.easyhttp.core.builder.HttpPostRequestBuilder;
import com.github.easyhttp.test.bo.Person;

import java.util.HashMap;
import java.util.Map;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/5/6 23:03
 */
public class PostRequestTest {


    public static void main(String[] args) {

        System.out.println(new HttpPostRequestBuilder().uri("http://localhost:8080/easy-http/postTest/json")
                .jsonEntity("{\"id\":1,\"code\":\"postTest-json\"}").executeAsString());

        Map<String, String> formParams = new HashMap<String, String>();
        formParams.put("id", "2");
        formParams.put("code", "postTest-form");
        System.out.println(new HttpPostRequestBuilder().uri("http://localhost:8080/easy-http/postTest/form")
                .formUrlencodedEntity(formParams).executeAsString());

        Person person = new HttpPostRequestBuilder().uri("http://localhost:8080/easy-http/postTest/json")
                .jsonEntity("{\"id\":1,\"code\":\"postTest-json\"}").executeAsObject(Person.class);
        System.out.printf("person id: %d, code: %s%n", person.getId(), person.getCode());
    }


}
