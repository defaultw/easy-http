package com.github.easyhttp.core.builder;

import com.github.easyhttp.common.serializer.SerializerManager;
import com.github.easyhttp.common.serializer.interfaces.SerializerService;
import com.github.easyhttp.core.listener.HttpRequestListener;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    private String uriStr;

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
            uriStr = uri;
            // 有路由参数占位符时先不创建实例
            String routeParamPrefix = "${";
            if (!uriStr.contains(routeParamPrefix)) {
                this.uri = new URI(uriStr);
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
        uriStr = uriStr.replace(String.format("${%s}", key), value);
        uri(uriStr);
        try {
            this.uri = new URI(uriStr);
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
     * 异步发起请求，并支持回执处理
     *
     * @param listener 监听是否成功，并继续后续逻辑
     */
    public void asyncExecuteAsString(HttpRequestListener<String> listener) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(this::executeAsString);
        future.whenComplete((result, e) -> {
            if (e == null) {
                listener.success(result);
            }
        }).exceptionally(e -> {
            listener.failure(new Exception(e));
            return null;
        });
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
     * 发起异步请求并将结果转换为指定类型对象
     *
     * @param clazz    指定对象类型
     * @param listener 监听是否成功，并继续后续逻辑
     */
    public <V> void asyncExecuteAsObject(Class<V> clazz, HttpRequestListener<V> listener) {
        CompletableFuture<V> future = CompletableFuture.supplyAsync(() -> executeAsObject(clazz));
        future.whenComplete((result, e) -> {
            if (e == null) {
                listener.success(result);
            }
        }).exceptionally(e -> {
            listener.failure(new Exception(e));
            return null;
        });
    }

    /**
     * 仅发起请求，不返回内容
     */
    public void executeAsNull() {
        executeAsString();
    }

    /**
     * 异步仅发起请求，不返回内容
     */
    public void asyncExecuteAsNull(HttpRequestListener<Void> listener) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(this::executeAsNull);
        future.whenComplete((result, e) -> {
            if (e == null) {
                listener.success(result);
            }
        }).exceptionally(e -> {
            listener.failure(new Exception(e));
            return null;
        });
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

    private TrustManager[] trustAllHttpsCertificates() {
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
        return trustManagers;
    }

    public T trustAllHttpsCert() {
        // 参考文档： https://www.cnblogs.com/james-roger/p/15209090.html
        TrustManager[] trustManagers = trustAllHttpsCertificates();
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.SSL);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            sslContext.init(null, trustManagers, new SecureRandom());
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        httpClient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext)).build();
        return (T) this;
    }
}
