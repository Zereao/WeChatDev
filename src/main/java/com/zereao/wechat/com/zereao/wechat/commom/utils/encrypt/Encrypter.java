package com.zereao.wechat.com.zereao.wechat.commom.utils.encrypt;

/**
 * @author Zereao
 * @version 2018/11/03  22:02
 */
public interface Encrypter {
    /**
     * 加密内容
     *
     * @param content 需要加密的内容
     * @return 加密后的内容
     */
    String encrypt(String content);
}
