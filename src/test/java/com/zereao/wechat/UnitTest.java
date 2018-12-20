package com.zereao.wechat;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.commom.utils.OkHttp3Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zereao
 * @version 2018/12/10  20:04
 */
public class UnitTest {
    private static Pattern articleIdPattern = Pattern.compile("id=(.*?)&");

    @Test
    public void test10() throws IOException {
        String share = "http://note.youdao.com/noteshare?id=3df33d89adae7e91dbdf697eb88d3075&sub=E889E1B585C84DF08D25D4B1CE654823";
//        https://note.youdao.com/yws/public/note/3df33d89adae7e91dbdf697eb88d3075?editorType=0
        String url = "https://note.youdao.com/yws/public/note/3df33d89adae7e91dbdf697eb88d3075?editorType=0";
//        String result = OkHttp3Utils.INSTANCE.doGet(url);

        Matcher matcher = articleIdPattern.matcher(share);
        String a = matcher.find() ? matcher.group() : "";
        System.out.println(a);
    }

    @Test
    public void test09() {
        String a = "";
        System.out.println(a.startsWith("1"));
    }

    @Test
    public void test8() {
        String code = "1-23-dasa";
        String result = code.replaceAll("-\\d+$", "-");
        System.out.println(result);
    }

    @Test
    public void test07() {
        List<Integer> a = new ArrayList<>();
        a.forEach(i -> {
            int b = 1;
            System.out.println(b);
            System.out.println(i);
        });
    }

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
