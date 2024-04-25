package com.github.easyhttp.core.builder;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/17 22:18
 */
public class HttpGetRequestBuilder extends AbstractHttpRequestBuilder<HttpGetRequestBuilder> {

    HttpGet httpGet;

    public HttpGetRequestBuilder() {
        super();
    }

    @Override
    protected CloseableHttpResponse executeInternal() throws Exception {
        if (uri != null) {
            // 构建查询参数
            URIBuilder uriBuilder = new URIBuilder(this.uri);
            queryParams.forEach(param -> uriBuilder.addParameter(param.getName(), param.getValue()));
            this.uri = uriBuilder.build();
            
            httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig);
            applyHeaders(httpGet);
            return httpClient.execute(httpGet);
        } else {
            throw new IllegalStateException("URI is not set. Please call 'uri' method first.");
        }
    }


}
