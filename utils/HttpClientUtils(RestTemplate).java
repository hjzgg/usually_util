import xxx.common.json.JsonUtil;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class HttpClientUtils {

    /**
     * 最大连接数
     */
    private static final int MAX_CONNECTION_TOTAL = 300;
    /**
     * 路由并发数
     */
    private static final int ROUTE_MAX_COUNT = 200;
    /**
     * 重试次数
     */
    private static final int RETRY_COUNT = 3;
    /**
     * 连接超时
     */
    private static final int CONNECTION_TIME_OUT = 45000;
    /**
     * 数据超时
     */
    private static final int READ_TIME_OUT = 75000;
    /**
     * 连接等待
     */
    private static final int CONNECTION_REQUEST_TIME_OUT = 5000;
    /**
     * 编码
     */
    private static final String CHARSET = "UTF-8";



    public static <T> T postForObject(String url, Object requestBody, MediaType contentType, Class<T> responseType) throws Exception {
        return postForObjectWithHeader(url, requestBody, contentType, responseType, null);
    }

    public static <T> T postForObjectWithHeader(String url, Object requestBody, MediaType contentType, Class<T> responseType, Map<String, String[]> headers) throws Exception {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = acceptsUntrustedCertsHttpClient();
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            clientHttpRequestFactory.setConnectTimeout(CONNECTION_TIME_OUT);
            clientHttpRequestFactory.setReadTimeout(READ_TIME_OUT);
            clientHttpRequestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
            clientHttpRequestFactory.setBufferRequestBody(false);

            RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

            // 添加内容转换器
            List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
            messageConverters.add(new StringHttpMessageConverter(Charset.forName(CHARSET)));
            restTemplate.setMessageConverters(messageConverters);

            // headers
            HttpHeaders httpHeaders = getDefaultHeader(contentType);
            if (!Objects.isNull(headers)) {
                fillHeaders(httpHeaders, headers);
            }

            HttpEntity<Object> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
            T response = restTemplate.postForObject(url, httpEntity, responseType);
            return response;
        } catch (Exception e) {
            throw new Exception("网络异常或请求错误.", e);
        } finally {
            if (httpClient != null)
                httpClient.close();
        }
    }

    public static <T> T getForObjectWithHeader(String url, Class<T> responseType, String headerName, String headerValue) throws Exception {
        HttpHeaders httpHeaders = getDefaultHeader(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.add(headerName, headerValue);
        return getForObject(url, responseType, httpHeaders);
    }

    public static <T> T getForObject(String url, Class<T> responseType) throws Exception {
        return getForObject(url, responseType, getDefaultHeader(MediaType.APPLICATION_JSON_UTF8));
    }

    private static HttpHeaders getDefaultHeader(MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.ALL);
        headers.setAccept(acceptableMediaTypes);
        headers.setContentType(contentType);
        headers.add("Connection", "Keep-Alive");
        headers.add("Content-Encoding", "gzip");
        headers.add("Vary", "Accept-Encoding");
        headers.add("Transfer-Encoding", "chunked");
        return headers;
    }

    private static <T> T getForObject(String url, Class<T> responseType, HttpHeaders httpHeaders) throws Exception {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = acceptsUntrustedCertsHttpClient();
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            clientHttpRequestFactory.setConnectTimeout(CONNECTION_TIME_OUT);
            clientHttpRequestFactory.setReadTimeout(READ_TIME_OUT);
            clientHttpRequestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
            clientHttpRequestFactory.setBufferRequestBody(false);

            RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

            // 添加内容转换器
            List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
            messageConverters.add(new StringHttpMessageConverter(Charset.forName(CHARSET)));
            restTemplate.setMessageConverters(messageConverters);

            HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);

            URI uri = new URI(url);
            String responseStr = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class).getBody();

            return JsonUtil.json2Java(responseStr, responseType).get();
        } catch (Exception e) {
            throw new Exception("网络异常或请求错误.", e);
        } finally {
            if (httpClient != null)
                httpClient.close();
        }
    }

    /**
     * 接受未信任的请求
     *
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static CloseableHttpClient acceptsUntrustedCertsHttpClient()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();

        httpClientBuilder.setSSLContext(sslContext);
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(MAX_CONNECTION_TOTAL);
        connMgr.setDefaultMaxPerRoute(ROUTE_MAX_COUNT);

        httpClientBuilder.setConnectionManager(connMgr);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, true));
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        CloseableHttpClient client = httpClientBuilder.build();

        return client;
    }

    public static <T> T getForObjectWithHeader(String url, Map<String, String[]> headers, Class<T> responseType) throws Exception {
        HttpHeaders httpHeaders = getDefaultHeader(MediaType.APPLICATION_JSON_UTF8);
        fillHeaders(httpHeaders, headers);
        return getForObject(url, responseType, httpHeaders);
    }

    private static void fillHeaders(HttpHeaders httpHeaders, Map<String, String[]> headers) {
        headers.entrySet().stream().forEach(header -> {
            Stream.of(header.getValue()).forEach(value -> {
                httpHeaders.add(header.getKey(), value);
            });
        });
    }
}
