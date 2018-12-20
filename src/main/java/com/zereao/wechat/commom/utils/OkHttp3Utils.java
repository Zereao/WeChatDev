package com.zereao.wechat.commom.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.annotations.EverythingIsNonNull;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Zereao
 * @version 2018/12/10  17:26
 */
@Slf4j
public enum OkHttp3Utils {
    /**
     * 单元素枚举实体
     */
    INSTANCE;

    /**
     * ContentType 内部枚举
     */
    public enum ContentType {
        /**
         * JSON数据 - UTF-8
         */
        CONTENT_TYPE_JSON_UTF8("application/json; charset=utf-8"),
        /**
         * 表单类型数据 - UTF-8
         */
        CONTENT_TYPE_FORM_UTF8("application/x-www-form-urlencoded; charset=utf-8");

        private MediaType type;

        ContentType(String type) {
            this.type = MediaType.parse(type);
        }

        public MediaType getType() {
            return type;
        }
    }

    private OkHttpClient client;

    OkHttp3Utils() {
        client = new OkHttpClient.Builder()
                // 设置连接超时
                .callTimeout(5, TimeUnit.SECONDS)
                // 设置读超时
                .readTimeout(5, TimeUnit.SECONDS)
                // 设置写超时
                .writeTimeout(5, TimeUnit.SECONDS)
                // 是否自动重连
                .retryOnConnectionFailure(true)
                // 设置连接池
                .connectionPool(new ConnectionPool(100, 30, TimeUnit.MINUTES))
                .build();
    }

    /**
     * 获取单例的OkHttpClient实例，方便自定义方法
     *
     * @return 单例的OkHttpClient实例
     */
    public static OkHttpClient getClient() {
        return INSTANCE.client;
    }

    /**
     * 发送Get请求
     *
     * @param url 请求Url链接
     * @return 请求结果
     * @throws IOException IOException
     */
    public String doGet(String url) throws IOException {
        log.info("========================= 准备发起GET请求：{} =========================", url);
        Request request = new Request.Builder().url(url).build();
        return this.sendRequest(request);
    }

    /**
     * 发送同步的Post请求，以表单方式传递参数
     *
     * @param url  请求Url链接
     * @param json 请求参数，可以传JSONObjct or Map
     * @return 请求结果
     */
    public String doPost(String url, Map<String, Object> json) throws IOException {
        log.info("========================= 准备发起POST-FORM请求：{} =========================", url);
        FormBody.Builder formEncodingBuilder = new FormBody.Builder(Charset.forName("UTF-8"));
        json.forEach((key, value) -> formEncodingBuilder.add(key, String.valueOf(value)));
        RequestBody formBody = formEncodingBuilder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        return this.sendRequest(request);
    }

    /**
     * 发送同步的Post请求，以JSONObject 的形式传递数据
     *
     * @param url  请求Url链接
     * @param json JSON 对象
     * @return 请求结果
     * @throws IOException IOException
     */
    public String doPostSync(String url, JSONObject json) throws IOException {
        log.info("========================= 准备POST-JSON同步请求：{} =========================", url);
        RequestBody requestBody = RequestBody.create(ContentType.CONTENT_TYPE_JSON_UTF8.getType(), json.toString());
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return this.sendRequest(request);
    }

    /**
     * 发送异步的Post请求，以JSONObject 的形式传递数据
     *
     * @param url  请求Url链接
     * @param json JSON 对象
     */
    public void doPostAsync(String url, JSONObject json) {
        log.info("========================= 准备POST-JSON异步请求：{} =========================", url);
        RequestBody requestBody = RequestBody.create(ContentType.CONTENT_TYPE_JSON_UTF8.getType(), json.toString());
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                String url = call.request().url().scheme();
                log.warn("------> 某次请求 {} 失败了！, {}", url, e);
            }

            @Override
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                String url = call.request().url().url().toString();
                log.info("------> 某次请求 {} 成功！,返回结果为 {}", url, INSTANCE.parseResponse(response));
            }
        });
        log.info("------> 请求发送成功！");
    }


    /**
     * 抽离的私有方法，发送请求，并对处理Response
     *
     * @param request Request
     * @return 解析出的结果字符串
     * @throws IOException IOException
     */
    private String sendRequest(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        return this.parseResponse(response);
    }

    /**
     * 抽离的公共方法，用于解析Response
     *
     * @param response Response
     * @return 解析出的结果字符串
     * @throws IOException IOException
     */
    private String parseResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("------> 出现未知错误！response = " + response);
        }
        ResponseBody body = response.body();
        String result = "";
        if (body == null) {
            log.info("------> 请求返回的数据体body为null！");
        } else {
            result = body.string();
        }
        log.info("========================= 请求完毕！result = {} =========================", StringUtils.isBlank(result) ? "null" : result);
        return result;
    }
}
