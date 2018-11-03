package com.zereao.wechat.com.zereao.wechat.commom.utils.encrypt;

import com.zereao.wechat.com.zereao.wechat.commom.utils.encrypt.provider.SHA1EncryptProvider;

/**
 * @author Zereao
 * @version 2018/11/03  22:04
 */
public class EncrypterFactory {
    private Encrypter encrypter = null;

    private enum EncrypterEnum {
        /**
         * 使用枚举实现的一个单例模式
         */
        INSTANCE;
        private SHA1EncryptProvider sha1Encrypter;

        EncrypterEnum() {
            sha1Encrypter = new SHA1EncryptProvider();
        }
    }

    public static Encrypter getInstance(EncryptAlgorithmEnum algorithm) {
        switch (algorithm) {
            case SHA1:
                return EncrypterEnum.INSTANCE.sha1Encrypter;
            default:
                return EncrypterEnum.INSTANCE.sha1Encrypter;
        }
    }

}
