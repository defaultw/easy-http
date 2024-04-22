package com.github.easy.http.client.core.serialize;

import java.util.List;

/**
 * 序列化统一入口
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/18 23:25
 */
public interface SerializerService {

    /**
     * 对象转json字符串
     *
     * @param object 回执对象
     * @return json字符串
     */
    String serializeObject(Object object);

    /**
     * 数组对象转json字符串
     *
     * @param array 数组对象
     * @return json字符串
     */
    String serializeArray(Object array);

    /**
     * json字符串转为指定类型对象
     *
     * @param json  json字符串
     * @param clazz 指定对象类型
     * @return json字符串对应的指定类型对象
     */
    <T> T deserialize(String json, Class<T> clazz);

    /**
     * 数组json字符串转List对象
     *
     * @param json  数组json字符串
     * @param clazz 指定对象类型
     * @return json字符串对应的指定类型对象
     */
    <T> List<T> deserializeArray(String json, Class<T> clazz);


}
