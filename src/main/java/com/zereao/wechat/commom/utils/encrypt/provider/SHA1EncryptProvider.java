package com.zereao.wechat.commom.utils.encrypt.provider;

import com.zereao.wechat.commom.utils.encrypt.Encrypter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Zereao
 * @version 2018/11/03  22:20
 */
@Slf4j
public class SHA1EncryptProvider implements Encrypter {
    @Override
    public String encrypt(String content) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest mdTemp = null;
        try {
            mdTemp = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA1加密MessageDigest获取失败！");
            return null;
        }
        mdTemp.update(content.getBytes(StandardCharsets.UTF_8));
        byte[] md = mdTemp.digest();
        int j = md.length;
        char[] buf = new char[j * 2];
        int k = 0;
        for (byte byte0 : md) {
            buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
            buf[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(buf);
    }
}
