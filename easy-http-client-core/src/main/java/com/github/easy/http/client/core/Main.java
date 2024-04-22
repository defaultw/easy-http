package com.github.easy.http.client.core;

import com.github.easy.http.client.core.serialize.SerializerFactory;
import com.github.easy.http.client.core.serialize.SerializerService;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/22 23:12
 */
public class Main {


    public static void main(String[] args) {

        String serializerName = "FastJsonSerializeServiceImpl";

        SerializerService serializer = SerializerFactory.getSerializer(serializerName);

        Persion persion = new Persion();
        persion.setName("www");
        persion.setSale("12333");

        System.out.println(serializer.serializeObject(persion));


    }





}

class Persion {
    private String name;

    private String sale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }
}
