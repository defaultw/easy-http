package com.github.easyhttp.core.builder;

import com.github.easyhttp.common.serializer.SerializerManager;
import com.github.easyhttp.common.serializer.interfaces.SerializerService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Request请求构建
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/17 21:43
 */
public abstract class AbstractHttpRequestBuilder<T extends AbstractHttpRequestBuilder<T>> {

    protected final Logger logger = LoggerFactory.getLogger(AbstractHttpRequestBuilder.class);

    protected CloseableHttpClient httpClient;

    protected URI uri;

    private String _uri;

    protected RequestConfig requestConfig;

    protected List<BasicNameValuePair> headers = new ArrayList<>();

    protected List<BasicNameValuePair> queryParams = new ArrayList<>();

    protected int timeout;

    protected AbstractHttpRequestBuilder() {
        this.httpClient = HttpClients.createDefault();
    }

    public T timeout(int timeoutMillis) {
        this.requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeoutMillis)
                .setConnectTimeout(timeoutMillis)
                .build();
        return (T) this;
    }

    public T uri(String uri) {
        try {
            _uri = uri;
            // 有路由参数占位符时先不创建实例
            String routeParamPrefix = "${";
            if (!_uri.contains(routeParamPrefix)) {
                this.uri = new URI(_uri);
            }
            return (T) this;
        } catch (URISyntaxException e) {
            logger.error("URI syntax exception", e);
            throw new RuntimeException("Invalid URL", e);
        }
    }

    public T header(String key, String value) {
        this.headers.add(new BasicNameValuePair(key, value));
        return (T) this;
    }

    public T queryParam(String key, String value) {
        this.queryParams.add(new BasicNameValuePair(key, value));
        return (T) this;
    }

    public T routeParam(String key, String value) {
        _uri = _uri.replace(String.format("${%s}", key), value);
        uri(_uri);
        try {
            this.uri = new URI(_uri);
            return (T) this;
        } catch (URISyntaxException e) {
            logger.error("URI syntax exception", e);
            throw new RuntimeException("Invalid URL", e);
        }
    }

    /**
     * 发起请求
     *
     * @return 回执
     */
    protected abstract CloseableHttpResponse executeInternal() throws Exception;

    protected void applyHeaders(HttpRequestBase request) {
        headers.forEach(header -> request.setHeader(header.getName(), header.getValue()));
    }

    /**
     * 发起请求并将结果转换为String返回
     *
     * @return 请求结果
     */
    public String executeAsString() {
        try (CloseableHttpResponse response = executeInternal()) {
            return new HttpResponseWrapper(response).getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发起请求并将结果转换为指定类型对象
     *
     * @param clazz 指定对象类型
     * @return 请求结果
     */
    public <V> V executeAsObject(Class<V> clazz) {
        SerializerService serializer = new SerializerManager().getSerializer();
        return serializer.deserialize(executeAsString(), clazz);
    }

    /**
     * 仅发起请求，不返回内容
     */
    public void executeAsNull() {
        executeAsString();
    }

    public HttpResponseWrapper execute() {
        CloseableHttpResponse response = null;
        try {
            response = executeInternal();
        } catch (Exception e) {
            logger.error("An error occurs when the [executeInternal] method is executed", e);
        }
        if (response != null) {
            return new HttpResponseWrapper(response);
        } else {
            logger.error("No response received.");
            return null;
        }
    }
}
