package com.github.easy.http.client.core.builder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * http请求构建
 *
 * @author wangxiaojiang
 * @date 2024/4/15 22:32
 */
public class EasyHttpClientBuilder {

    private URI uri;

    private final List<BasicNameValuePair> params = new ArrayList<>();

    private HttpEntity entity;

    /**
     * url构建
     *
     * @param uri 请求地址
     * @return Builder对象
     */
    public EasyHttpClientBuilder uri(String uri) throws URISyntaxException {
        this.uri = new URI(uri);
        return this;
    }

    /**
     * 请求参数构建
     *
     * @param key   参数键
     * @param value 参数值
     * @return Builder对象
     */
    public EasyHttpClientBuilder params(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
        return this;
    }

    /**
     * get请求
     *
     * @return 返回请求结果
     */
    public HttpResponse get() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URIBuilder uriBuilder = new URIBuilder(uri);
            uriBuilder.setParameters((NameValuePair) params);
            HttpGet httpGet = new HttpGet(uriBuilder.toString());
            return httpClient.execute(httpGet);
        }
    }


}
