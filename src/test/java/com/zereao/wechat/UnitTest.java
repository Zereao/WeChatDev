package com.zereao.wechat;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.utils.OkHttp3Utils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Zereao
 * @version 2018/12/10  20:04
 */
public class UnitTest {
    @Test
    public void test1() throws IOException {
        OkHttp3Utils.INSTANCE.doPostAsync("https://www.baidu.com", new JSONObject());
    }
}
