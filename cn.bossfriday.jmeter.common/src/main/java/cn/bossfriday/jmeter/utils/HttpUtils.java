package cn.bossfriday.jmeter.utils;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * HttpUtils
 *
 * @author chenx
 */
public class HttpUtils {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslConnectionSocketFactory = null;
    private static SSLContextBuilder builder = null;

    private HttpUtils() {
        // just a private constructor
    }

    static {
        try {
            builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    // TrustAll
                    return true;
                }
            });

            sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"},
                    null,
                    NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getPoolingHttpClient
     *
     * @param isHttps
     * @return
     */
    public static CloseableHttpClient getPoolingHttpClient(boolean isHttps) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(1000);
        connectionManager.setDefaultMaxPerRoute(500);

        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .setSoReuseAddress(true)
                .setSoLinger(60)
                .setSoTimeout(500)
                .setSoKeepAlive(true)
                .build();
        connectionManager.setDefaultSocketConfig(socketConfig);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(10000)
                .build();

        HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);

        ConnectionKeepAliveStrategy connectionKeepAliveStrategy = (httpResponse, httpContext) -> 60 * 1000;

        if (isHttps) {
            return HttpClients.custom()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(requestRetryHandler)
                    .setKeepAliveStrategy(connectionKeepAliveStrategy)
                    .build();
        }

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(requestRetryHandler)
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                .build();
    }

    /**
     * getUrl
     */
    public static String getUrl(String url, boolean isHttps) {
        if (isHttps) {
            return HTTPS + "://" + url;
        }

        return HTTP + "://" + url;
    }
}
