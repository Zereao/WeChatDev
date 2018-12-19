package com.zereao.wechat;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.commom.utils.OkHttp3Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zereao
 * @version 2018/12/10  20:04
 */
public class UnitTest {
    @Test
    public void test06() {
        Integer a = Stream.of(1, 2, 3, 4, 5).filter(i -> i > 2).findFirst().orElse(null);
        System.out.println(a);
    }

    @Test
    public void test05() {
        MsgType a = MsgType.of("shortvideo");
        System.out.println(a);
    }

    @Test
    public void test04() throws ParseException {
        long a = 1348831860L;
        final String STANDARD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        Date s = new SimpleDateFormat(STANDARD_DATE_FORMAT).parse(String.valueOf(a));
        System.out.println(s);
    }

    @Test
    public void test03() {
        MsgType a = MsgType.SHORT_VIDEO;
    }

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
