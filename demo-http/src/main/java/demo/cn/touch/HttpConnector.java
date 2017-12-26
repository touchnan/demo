/*
 */
package demo.cn.touch;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * Aug 21, 2012
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class HttpConnector {
    private static PoolingHttpClientConnectionManager cm;

    RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH)
            .setExpectContinueEnabled(true).setStaleConnectionCheckEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

    private HttpClient client;

    private ResponseHandler<String> stringResponseHandler = new BasicResponseHandler() {
        public String handleResponse(final HttpResponse response) throws HttpResponseException, IOException {
            final StatusLine statusLine = response.getStatusLine();
            final HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300 && statusLine.getStatusCode()!=302) {
                EntityUtils.consume(entity);
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
            return entity == null ? null : EntityUtils.toString(entity);
        }
    };

    private ResponseHandler<byte[]> byteResponseHandler = new ResponseHandler<byte[]>() {
        public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300 && statusLine.getStatusCode()!=302) {
                EntityUtils.consume(entity);
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
            return ((entity == null) ? null : EntityUtils.toByteArray(entity));
        }
    };

    public static HttpClientConnectionManager getConnManager() {
        return cm;
    }

    public HttpConnector() {
        this(60000, 120000);// 连接超时1分钟，读数据(连接后)超时2分钟
    }

    public HttpConnector(CookieStore cookieStore) {
        this(cookieStore, 60000, 120000);
    }

    /**
     * @param connectTimeOut
     *            连接超时时间
     * @param readTimeOut
     *            读取超时时间
     */
    public HttpConnector(int connectTimeOut, int readTimeOut) {
        this(null, connectTimeOut, readTimeOut);
    }

    public HttpConnector(CookieStore cookieStore, int connectTimeOut, int readTimeOut) {
        this(cookieStore, null, null, connectTimeOut, readTimeOut);
    }

    public HttpConnector(HttpHost proxy, UsernamePasswordCredentials creds) {
        this(null, proxy, creds);// 连接超时1分钟，读数据(连接后)超时2分钟
    }

    public HttpConnector(CookieStore cookieStore, HttpHost proxy, UsernamePasswordCredentials creds) {
        this(cookieStore, proxy, creds, 60000, 120000);// 连接超时1分钟，读数据(连接后)超时2分钟
    }

    public HttpConnector(CookieStore cookieStore, HttpHost proxy, UsernamePasswordCredentials creds,
            int connectTimeOut, int readTimeOut) {
        // HttpHost proxy = new HttpHost("proxy.tt", 8080);
        // UsernamePasswordCredentials creds = new UsernamePasswordCredentials("fttj", "ft07");
        // BasicCookieStore cookieStore = new BasicCookieStore();
        RequestConfig requestConfig =
                RequestConfig.copy(defaultRequestConfig).setSocketTimeout(readTimeOut)
                        .setConnectTimeout(connectTimeOut).setConnectionRequestTimeout(connectTimeOut).build();

        // 验证信息不为空
        CredentialsProvider credsProvider = null;
        if (creds != null) {
            // 实例化验证
            credsProvider = new BasicCredentialsProvider();
            // 创建验证
            credsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
        }

        this.client =
                HttpClients.custom().setConnectionManager(cm).setDefaultCookieStore(cookieStore)
                        .setDefaultCredentialsProvider(credsProvider).setProxy(proxy)// 创建代理
                        .setDefaultRequestConfig(requestConfig).build();

    }

    public static void setMaxConns(int maxTotal, int maxPerRoute) {
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
    }

    public String get(String url) {
        return get(new HttpGet(url), stringResponseHandler);
    }

    public String get(HttpGet httpget) {
        return this.get(httpget, stringResponseHandler);
    }

    public <T> T get(HttpGet httpget, ResponseHandler<T> responseHandler) {
        try {
            return client.execute(httpget, responseHandler);
        } catch (Exception e) {
            LogFactory.getLog(HttpConnector.class).error(e);
            httpget.abort();
            throw new RuntimeException(e);
        }
    }

    public byte[] getBytes(String url) {
        return get(new HttpGet(url), byteResponseHandler);
    }

    public String get(String url, String queryStr) {
        if (!StringUtils.isBlank(queryStr)) {
            url =
                    url + (url.indexOf(Constants.QUESTION_MARK) != (-1) ? Constants.AND : Constants.QUESTION_MARK)
                            + queryStr;
        }
        return get(url);
    }

    public byte[] getBytes(String url, String queryStr) {
        if (!StringUtils.isBlank(queryStr)) {
            url =
                    url + (url.indexOf(Constants.QUESTION_MARK) != (-1) ? Constants.AND : Constants.QUESTION_MARK)
                            + queryStr;
        }
        return getBytes(url);
    }

    public String get(String url, List<NameValuePair> nvps) {
        return get(url, URLEncodedUtils.format(nvps, Consts.UTF_8));
    }

    public byte[] getBytes(String url, List<NameValuePair> nvps) {
        return getBytes(url, URLEncodedUtils.format(nvps, Consts.UTF_8));
    }

    public String post(String url) {
        return postExecute(url, null);
    }

    public byte[] postBytes(String url) {
        return postBytesExecute(url, null);
    }

    public String post(HttpPost httpost) {
        return postExecute(httpost, null);
    }

    public byte[] postBytes(HttpPost httpost) {
        return postBytesExecute(httpost, null);
    }

    public String post(String url, List<NameValuePair> nvps) {
        return postExecute(url, new UrlEncodedFormEntity(nvps, Consts.UTF_8));
    }

    public byte[] postBytes(String url, List<NameValuePair> nvps) {
        return postBytesExecute(url, new UrlEncodedFormEntity(nvps, Consts.UTF_8));
    }

    public String post(HttpPost httpost, List<NameValuePair> nvps) {
        return postExecute(httpost, new UrlEncodedFormEntity(nvps, Consts.UTF_8));
    }

    public byte[] postBytes(HttpPost httpost, List<NameValuePair> nvps) {
        return postBytesExecute(httpost, new UrlEncodedFormEntity(nvps, Consts.UTF_8));
    }

    public String post4Parameters(String url, String parameters) {
        List<NameValuePair> nvps = URLEncodedUtils.parse(parameters, Consts.UTF_8);
        return postExecute(url, new UrlEncodedFormEntity(nvps, Consts.UTF_8));
    }

    public byte[] postBytes4Parameters(String url, String parameters) {
        List<NameValuePair> nvps = URLEncodedUtils.parse(parameters, Consts.UTF_8);
        return postBytesExecute(url, new UrlEncodedFormEntity(nvps, Consts.UTF_8));
    }

    public String post4Parameters(HttpPost httpost, String parameters) {
        List<NameValuePair> nvps = URLEncodedUtils.parse(parameters, Consts.UTF_8);
        return postExecute(httpost, new UrlEncodedFormEntity(nvps, Consts.UTF_8));
    }

    public byte[] postBytes4Parameters(HttpPost httpost, String parameters) {
        List<NameValuePair> nvps = URLEncodedUtils.parse(parameters, Consts.UTF_8);
        return postBytesExecute(httpost, new UrlEncodedFormEntity(nvps, Consts.UTF_8));
    }

    public String post4Stream(String url, String data) {
        return postExecute(url, new StringEntity(data, Consts.UTF_8));
    }

    public byte[] postBytes4Stream(String url, String data) {
        return postBytesExecute(url, new StringEntity(data, Consts.UTF_8));
    }

    public String post4Stream(HttpPost httpost, String data) {
        return postExecute(httpost, new StringEntity(data, Consts.UTF_8));
    }

    public byte[] postBytes4Stream(HttpPost httpost, String data) {
        return postBytesExecute(httpost, new StringEntity(data, Consts.UTF_8));
    }

    public String postExecute(String url, HttpEntity entity) {
        return postExecute(new HttpPost(url), entity, stringResponseHandler);
    }

    public byte[] postBytesExecute(String url, HttpEntity entity) {
        return postExecute(new HttpPost(url), entity, byteResponseHandler);
    }

    public String postExecute(HttpPost httpost, HttpEntity entity) {
        return postExecute(httpost, entity, stringResponseHandler);
    }

    public byte[] postBytesExecute(HttpPost httpost, HttpEntity entity) {
        return postExecute(httpost, entity, byteResponseHandler);
    }

    public <T> T postExecute(HttpPost httpost, HttpEntity entity, ResponseHandler<T> responseHandler) {
        if (entity != null) {
            httpost.setEntity(entity);
        }
        try {
            return client.execute(httpost, responseHandler);
        } catch (Exception e) {
            LogFactory.getLog(HttpConnector.class).error(e);
            httpost.abort();
            throw new RuntimeException(e);
        }
    }

    static {

        /**
         * 最大连接数
         */
        int MAX_TOTAL_CONNECTIONS = 800;
        /**
         * 每个路由最大连接数
         */
        int MAX_ROUTE_CONNECTIONS = 400;

        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);// 设置连接最大数
        cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);// 设置每个Route的连接最大数
    }

}
