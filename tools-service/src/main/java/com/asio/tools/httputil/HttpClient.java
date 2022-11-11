package com.asio.tools.httputil;

import cn.hutool.core.util.StrUtil;
import com.asio.tools.jsonutil.JsonUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @ClassName: HttpClient
 * @author: leijun
 * @creat: 2021/4/6 20:15
 * @version: 1.0
 * 描述:
 */
public class HttpClient {
    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    /**
     * 发送post请求获取结果
     * @param url 请求链接
     * @param paramsMap 参数集合
     * @param clazz<T> 目标对象class
     * @return T 制定类型对象
     * @throws Exception
     */
    public static <T> T postRequestJson(String url, Map<String, Object> paramsMap, Class<T> clazz) throws Exception {
        String json = parseMapToJson(paramsMap);
        String bodyJson = HttpUtil.sendPostDataByJson(url, json);
        if (StrUtil.isBlank(bodyJson)) {
            logger.error("发送Map数据的响应未获取到结果！" );
            return null;
        }
        Gson gson = new Gson();
        T t = gson.fromJson(bodyJson, clazz);
        return t;
    }

    /**
     * 发送post请求获取结果
     * @param url 请求链接
     * @param paramsMap 参数集合
     * @return Map<String, Object> 返回Map对象
     * @throws Exception
     */
    public static Map<String, Object> postRequestMap(String url, Map<String, Object> paramsMap) throws Exception {
        String json = parseMapToJson(paramsMap);
        String bodyJson = HttpUtil.sendPostDataByJson(url, json);
        if (StrUtil.isBlank(bodyJson)) {
            logger.error("发送Map数据的响应未获取到结果！" );
            return null;
        }
        Map<String, Object> resultMap = parseJsonToMap(bodyJson);
        return resultMap;
    }

    /**
     * 将Json字符串转成Map
     * @param jsonString
     * @return map
     */
    public static Map parseJsonToMap(String jsonString) throws Exception {
        Map map = JsonUtil.fromJson(jsonString, Map.class);
        logger.info("Json转Map-->:");
       /* for (Object obj : map.keySet()) {
            logger.info(obj + "-" + map.get(obj));
        }*/
        return map;
    }

    /**
     * 将Map转换成Json
     * @param map
     * @return
     */
    public static String parseMapToJson(Map<String, Object> map) throws Exception {
        String json = JsonUtil.toJson(map);
        logger.info("Map转Json-->:");
        logger.info(json);
        return json;
    }
}
