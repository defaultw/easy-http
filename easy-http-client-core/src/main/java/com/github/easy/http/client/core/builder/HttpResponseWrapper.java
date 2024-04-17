package com.github.easy.http.client.core.builder;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/17 22:06
 */
public class HttpResponseWrapper {

    private final CloseableHttpResponse response;

    public HttpResponseWrapper(CloseableHttpResponse response) {
        this.response = response;
    }

    public int getStatusLineCode() {
        return response.getStatusLine().getStatusCode();
    }

    public String getContentAsString() {
        try {
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Header[] getHeaders() {
        return response.getAllHeaders();
    }

    public void closeResponse() {
        try {
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
