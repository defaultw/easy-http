package com.github.easy.http.client.core.serialize.provider;

import com.github.easy.http.client.core.serialize.service.JsonService;

/**
 * json序列化服务提供者
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/18 23:37
 */
public interface JsonServiceProvider {

    /**
     * 判断是否存在json序列化服务
     *
     * @return true存在，false不存在
     */
    boolean isAvailable();

    /**
     * 创建json序列化服务
     *
     * @return json序列化服务
     */
    JsonService createJsonService();


}
