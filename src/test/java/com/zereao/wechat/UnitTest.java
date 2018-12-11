package com.zereao.wechat;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.utils.OkHttp3Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zereao
 * @version 2018/12/10  20:04
 */
public class UnitTest {
    @Test
    public void test02() {
        String token = "lovebluesky";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(154);
        String resultStr = Stream.of(token, timestamp, nonce).sorted().collect(Collectors.joining());
        String encryptedStr = DigestUtils.sha1Hex(resultStr);
        System.out.println(token);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(encryptedStr);
    }

    @Test
    public void test1() throws IOException {
        OkHttp3Utils.INSTANCE.doPostAsync("https://www.baidu.com", new JSONObject());
    }
}
