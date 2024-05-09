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

        System.out.println(new HttpGetRequestBuilder().uri("http://localhost:8080/easy-http/getTest")
                .queryParam("id", "1").queryParam("code", "CM_12123123").executeAsString());

        Person person = new HttpGetRequestBuilder().uri("http://localhost:8080/easy-http/getTest")
                .queryParam("id", "1").queryParam("code", "CM_12123123").executeAsObject(Person.class);

        System.out.printf("person id: %d, code: %s%n", person.getId(), person.getCode());
        
    }


}

class Person {
    private Integer id;
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", code='" + code + '\'' +
                '}';
    }
}
