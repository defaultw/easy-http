package com.github.easy.http.client.core.builder;

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
            this.uri = new URI(uri);
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

    /**
     * 发起请求
     *
     * @return 回执
     */
    protected abstract CloseableHttpResponse executeInternal() throws Exception;

    protected void applyHeaders(HttpRequestBase request) {
        headers.forEach(header -> request.setHeader(header.getName(), header.getValue()));
    }

    public String executeAsString() {
        try (CloseableHttpResponse response = executeInternal()) {
            return new HttpResponseWrapper(response).getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
