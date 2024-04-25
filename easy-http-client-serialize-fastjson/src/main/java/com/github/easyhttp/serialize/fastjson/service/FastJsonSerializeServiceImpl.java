package com.github.easyhttp.serialize.fastjson.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.easyhttp.common.serializer.interfaces.SerializerService;

import java.util.List;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/22 22:46
 */
public class FastJsonSerializeServiceImpl implements SerializerService {


    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String serializeObject(Object object) {
        return JSONObject.toJSONString(object);
    }

    @Override
    public String serializeArray(Object array) {
        return JSONArray.toJSONString(array);
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }

    @Override
    public <T> List<T> deserializeArray(String json, Class<T> clazz) {
        return JSONArray.parseArray(json, clazz);
    }
}
