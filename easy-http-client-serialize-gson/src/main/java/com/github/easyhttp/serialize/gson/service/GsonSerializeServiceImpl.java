package com.github.easyhttp.serialize.gson.service;

import com.github.easyhttp.common.serializer.interfaces.SerializerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/5/7 23:30
 */
public class GsonSerializeServiceImpl implements SerializerService {
    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String serializeObject(Object object) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(object);
    }

    @Override
    public String serializeArray(Object array) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(array);
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.fromJson(json, clazz);
    }

    @Override
    public <T> List<T> deserializeArray(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }
}
