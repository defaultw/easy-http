package com.github.easyhttp.core.listener;

/**
 * Http请求监听
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/5/15 22:17
 */
public interface HttpRequestListener<T> {

    /**
     * 请求成功回调
     *
     * @param data 返回请求结果
     */
    void success(T data);

    /**
     * 请求失败回调
     *
     * @param e 返回异常
     */
    void failure(Exception e);

}
