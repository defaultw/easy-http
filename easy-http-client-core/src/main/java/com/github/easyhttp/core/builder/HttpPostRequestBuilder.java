package com.github.easyhttp.core.builder;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/17 22:23
 */
public class HttpPostRequestBuilder extends AbstractHttpRequestBuilder<HttpPostRequestBuilder> {

    private HttpPost httpPost;

    private HttpEntity httpEntity;

    private final List<BasicNameValuePair> formParams = new ArrayList<>();

    public HttpPostRequestBuilder() {
        super();
    }

    public HttpPostRequestBuilder jsonEntity(String jsonData) {
        httpEntity = new StringEntity(jsonData, ContentType.APPLICATION_JSON);
        return this;
    }

    public HttpPostRequestBuilder formUrlencodedEntity(Map<String, String> formData) {
        httpEntity = new UrlEncodedFormEntity(formData.entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()), StandardCharsets.UTF_8);
        return this;
    }

    public HttpPostRequestBuilder formField(String key, String value) {
        formParams.add(new BasicNameValuePair(key, value));
        return this;
    }

    public HttpPostRequestBuilder formUrlencodedEntity() {
        httpEntity = new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8);
        return this;
    }

    public HttpPostRequestBuilder multiPartFormData(Map<String, String> textParts, Map<String, File> fileParts) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        textParts.forEach(builder::addTextBody);
        fileParts.forEach((name, file) -> {
            builder.addBinaryBody(name, file, ContentType.DEFAULT_BINARY, file.getName());
        });
        httpEntity = builder.build();
        return this;
    }


    @Override
    protected CloseableHttpResponse executeInternal() throws IOException {
        if (uri != null && httpEntity != null) {
            httpPost = new HttpPost(uri);
            applyHeaders(httpPost);
            httpPost.setEntity(httpEntity);
            httpPost.setConfig(requestConfig);
            return httpClient.execute(httpPost);
        } else {
            throw new IllegalStateException("Either URI or Entity is not set. " +
                    "Please call 'uri' and 'jsonEntity/formUrlencodedEntity' methods first.");
        }
    }
}
