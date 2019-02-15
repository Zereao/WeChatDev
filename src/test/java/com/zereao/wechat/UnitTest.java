package com.zereao.wechat;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.commom.utils.OkHttp3Utils;
import com.zereao.wechat.commom.utils.ThreadPoolUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zereao
 * @version 2018/12/10  20:04
 */
class UnitTest {
    @Test
    void test19() {

    }


    @Data
    @AllArgsConstructor
    private class A {
        private String a;
        private Integer b;
    }

    @Test
    void test18() {
        for (int i = 0; i < 5; i++) {
            A a;
            if (i % 2 == 0) {
                a = new A("张三", 25 * i);
            } else {
                a = null;
            }
            System.out.println();
            A result = Optional.ofNullable(a).orElse(null);
            System.out.println(result);
        }

    }

    @Test
    void test17() {
        String a = "  祈福    嫁娶   　移徙  　纳财　 入宅　开市 作灶 破土　启鑽　修坟　";
        String as = a.replaceAll("[ 　]+", " ");
        System.out.println(a);
        System.out.println(as);
    }

    @Test
    void test16() {
        String[] a = new String[]{"1", "1", "1", "1", "1", "1", "1", "1"};
        System.out.println(a.length);
        List<String> as = new ArrayList<>();
        Collections.addAll(as, a);
        System.out.println(as);
    }

    @Test
    void test15() throws IOException {
        String url = "http://m.laohuangli.net/";
        String result = OkHttp3Utils.doGetWithGBK(url);
        System.out.println(result);
    }

    @Test
    void test144() {
        String packageName = "com.zereao.wechat";
        Package pkg = Package.getPackage(packageName);
        Annotation[] annotations = pkg.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation.toString());
        }

    }

    @Test
    void test14() {
        String a = "/a//gv///c/////d////f";
        System.out.println(a.replaceAll("//+", "/"));
    }

    @Test
    void test13() {
        StringBuilder sb = new StringBuilder("亲爱的、张安、三、萨达瓦打我、反而、");
        System.out.println(sb.toString());
        StringBuilder a = sb.deleteCharAt(sb.lastIndexOf("、"));
        System.out.println(sb);
        System.out.println(a);
    }

    private class TestThreadClass implements Callable<String> {
        private String index;

        TestThreadClass(String index) {
            this.index = index;
        }

        @Override
        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(5);
            return index;
        }
    }

    @Test
    void test12() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 5; i++) {
            String index = ThreadPoolUtils.submit(new TestThreadClass(String.valueOf(i)));
        }

    }

    private static Pattern articleIdPattern = Pattern.compile("id=(.*?)&");

    @Test
    void test11() {
        String share = "1-root.add[xlczj]http://note.youdao.com/noteshare?" +
                "id=3df33d89adae7e91dbdf697eb88d3075&sub=E889E1B585C84DF08D25D4B1CE654823[xlczj]" +
                "https://note.youdao.com/yws//note/3df33d89adae7e91dbdf697eb88d3075?editorType=0";
        String a = share.replaceAll("^*+\\[xlczj]?", "");
        System.out.println(share);
        System.out.println(a);
        System.out.println(a.replace("1-root.add[xlczj]", ""));
    }

    @Test
    void test10() throws IOException {
        String share = "http://note.youdao.com/noteshare?id=3df33d89adae7e91dbdf697eb88d3075&sub=E889E1B585C84DF08D25D4B1CE654823";
//        https://note.youdao.com/yws//note/3df33d89adae7e91dbdf697eb88d3075?editorType=0
        String url = "https://note.youdao.com/yws//note/3df33d89adae7e91dbdf697eb88d3075?editorType=0";
//        String result = OkHttp3Utils.INSTANCE.doGet(url);

        Matcher matcher = articleIdPattern.matcher(share);
        String a = matcher.find() ? matcher.group() : "";
        System.out.println(a);
    }

    @Test
    void test09() {
        String a = "";
        System.out.println(a.startsWith("1"));
    }

    @Test
    void test8() {
        String code = "1-23-dasa";
        String result = code.replaceAll("-\\d+$", "-");
        System.out.println(result);
    }

    @Test
    void test07() {
        List<Integer> a = new ArrayList<>();
        a.forEach(i -> {
            int b = 1;
            System.out.println(b);
            System.out.println(i);
        });
    }

    @Test
    void test06() {
        Integer a = Stream.of(1, 2, 3, 4, 5).filter(i -> i > 2).findFirst().orElse(null);
        System.out.println(a);
    }

    @Test
    void test05() {
        MsgType a = MsgType.of("shortvideo");
        System.out.println(a);
    }

    @Test
    void test04() throws ParseException {
        long a = 1348831860L;
        final String STANDARD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        Date s = new SimpleDateFormat(STANDARD_DATE_FORMAT).parse(String.valueOf(a));
        System.out.println(s);
    }

    @Test
    void test03() {
        MsgType a = MsgType.SHORT_VIDEO;
    }

    @Test
    void test02() {
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

    static void main(String[] args) throws ClassNotFoundException {
        Class c = Class.forName("com.zereao.wechat.commom.utils.OkHttp3Utils$ContentType");
        Class b = Class.forName("com.zereao.wechat.commom.utils.OkHttp3Utils");
        Class a = Class.forName("com.zereao.wechat.commom.utils.OkHttp3Utils$Instance");
        List<Class> ss = new ArrayList<>();
        ss.add(b);
        ss.add(c);
        ss.add(a);
        System.out.println(ss);
    }

    @Test
    void test1() throws ClassNotFoundException {
        Class c = Class.forName("com.zereao.wechat.commom.utils.OkHttp3Utils$ContentType");
        Class b = Class.forName("com.zereao.wechat.commom.utils.OkHttp3Utils");
        Class a = Class.forName("com.zereao.wechat.commom.utils.OkHttp3Utils$Instance");
        List<Class> ss = new ArrayList<>();
        ss.add(b);
        ss.add(c);
        ss.add(a);

        System.out.println(ss);

    }
}
