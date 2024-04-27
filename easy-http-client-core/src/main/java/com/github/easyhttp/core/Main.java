package com.github.easyhttp.core;

import com.github.easyhttp.common.serializer.SerializerManager;
import com.github.easyhttp.common.serializer.interfaces.SerializerService;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/22 23:12
 */
public class Main {

    public void printf() {
        SerializerManager serializerManager = new SerializerManager();
        SerializerService serializer = serializerManager.getSerializer();

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
