package com.zereao.wechat.commom.utils.encrypt;

/**
 * @author Zereao
 * @version 2018/11/03  22:25
 */
public enum EncryptAlgorithmEnum {
    /**
     * SHA1 加密
     */
    SHA1("SHA1");

    private String algorithmName;

    EncryptAlgorithmEnum(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }
}
