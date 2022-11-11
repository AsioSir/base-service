package com.asio.tools.httputil;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @ClassName: HttpUtil
 * @author: leijun
 * @creat: 2021/4/6 18:00
 * @version: 1.0
 * 描述:
 */
public class HttpUtil {
    private final static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    //* 线程池管理
    private static final PoolingHttpClientConnectionManager POOL_HTTP_CON_MANAGER;

    //请求器配置
    public final static RequestConfig HTTP_CONFIG;

    private final static CloseableHttpClient HTTP_CLIENT;

    // 连接超时时间:6S
    private final static int CONN_TIMEOUT = 6 * 1000;
    // socket传输超时时间:6S
    private final static int CONN_REQUEST_TIMEOUT = 6 *1000;
    // 获取连接的超时时间:3S
    private final static int SOCKET_TIMEOUT = 3 *1000;


    static {
        POOL_HTTP_CON_MANAGER = new PoolingHttpClientConnectionManager();
        //最大连接数
        POOL_HTTP_CON_MANAGER.setMaxTotal(100);
        //路由基数
        POOL_HTTP_CON_MANAGER.setDefaultMaxPerRoute(20);
        //HTTP设置
        HTTP_CONFIG = RequestConfig.custom()
                .setConnectTimeout(CONN_TIMEOUT) //连接超时时间
                .setConnectionRequestTimeout(CONN_REQUEST_TIMEOUT) //从连接池中取的连接的最长时间
                .setSocketTimeout(SOCKET_TIMEOUT) //数据传输的超时时间
                .setStaleConnectionCheckEnabled(true) //提交请求前测试连接是否可用
                .build();
        HTTP_CLIENT = getConnection(true);
    }

    //获得HttpClient连接
    public static CloseableHttpClient getConnection(Boolean isPool) {
        CloseableHttpClient httpClient = null;

        if (isPool == true){
            //连接池方式

            //自定义重试机制，重试3次
            HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
                //重试次数
                int retryCount = 3;

                @Override
                public boolean retryRequest(IOException arg0, int retryTimes, HttpContext arg2) {
                    if (retryTimes >= retryCount) {
                        // Do not retry if over max retry count
                        return false;
                    }
                    if (arg0 instanceof UnknownHostException || arg0 instanceof ConnectTimeoutException
                            || !(arg0 instanceof SSLException) || arg0 instanceof NoHttpResponseException
                            || arg0 instanceof SocketTimeoutException) {
                        return true;
                    }

                    HttpClientContext clientContext = HttpClientContext.adapt(arg2);
                    HttpRequest request = clientContext.getRequest();
                    boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                    if (idempotent) {
                        // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
                        return true;
                    }
                    return false;
                }
            };
            httpClient = HttpClients.custom()
                    // 设置连接池管理
                    .setConnectionManager(POOL_HTTP_CON_MANAGER)
                    // 设置请求配置
                    .setDefaultRequestConfig(HTTP_CONFIG)
                    // 设置重试次数
                    .setRetryHandler(handler)
                    .build();

            if (POOL_HTTP_CON_MANAGER != null && POOL_HTTP_CON_MANAGER.getTotalStats() != null) {
                log.info("创建新 client pool {}", POOL_HTTP_CON_MANAGER.getTotalStats().toString());
            }
        } else {
            //单个连接
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    /**
     * post请求传输map数据
     *
     * @param url url地址
     * @param map map数据
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByMap(String url, Map<String, Object> map) throws ClientProtocolException, IOException {
        String result = "";
        CloseableHttpResponse response = null;
        try {
            // 创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);
            // 装填参数
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
            }
            // 设置参数到请求对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

            // 设置header信息
            // 指定报文头【Content-type】、【User-Agent】
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 执行请求操作，并拿到结果（同步阻塞）
            response = HTTP_CLIENT.execute(httpPost);
            // 获取结果实体
            // 判断网络连接状态码是否正常(0--200都是正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            log.error("发送POST请求失败.....", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                    log.info("关闭POST连接请求！");
                }
            } catch (IOException e) {
                log.error("关闭发送POST请求失败！");
                e.printStackTrace();
            }
        }
        log.info("POST请求接口返回的数据：{}", result);
        return result;
    }

    /**
     * post请求传输json数据
     *
     * @param url  url地址
     * @param json json数据
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByJson(String url, String json) throws ClientProtocolException, IOException {
        String result = "";
        CloseableHttpResponse response = null;
        try {
            // 创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(HTTP_CONFIG);

            //TODO 验证信息
            httpPost.setHeader("api_key", "BusinessFinanceServiceKey1");

            // 设置参数到请求对象中
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            stringEntity.setContentEncoding("utf-8");
            httpPost.setEntity(stringEntity);
            // 执行请求操作，并拿到结果
            response = HTTP_CLIENT.execute(httpPost);

            // 获取结果实体
            // 判断网络连接状态码是否正常(0--200都是正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            log.error("发送POST请求失败.....", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                    log.info("关闭POST连接请求！");
                }
            } catch (IOException e) {
                log.error("关闭发送POST请求失败！");
                e.printStackTrace();
            }
        }
        log.info("POST请求接口返回的数据：{}", result);
        return result;
    }

    /**
     * get请求传输数据
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendGetData(String url) throws ClientProtocolException, IOException {
        String result = "";
        CloseableHttpResponse response = null;

        try {
            // 创建get方式请求对象
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-type", "application/json");
            // 通过请求对象获取响应对象
            response = HTTP_CLIENT.execute(httpGet);

            // 获取结果实体
            // 判断网络连接状态码是否正常(0--200都是正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            log.error("发送Get请求失败.....", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                    log.info("关闭连接请求！");
                }
            } catch (IOException e) {
                log.error("关闭发送Get请求失败！");
                e.printStackTrace();
            }
        }
        log.info("Get请求接口返回的数据：{}", result);
        return result;
    }

}
