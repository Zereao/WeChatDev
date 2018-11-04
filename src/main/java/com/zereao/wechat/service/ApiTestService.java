package com.zereao.wechat.service;

import com.zereao.wechat.commom.utils.encrypt.EncryptAlgorithmEnum;
import com.zereao.wechat.commom.utils.encrypt.Encrypter;
import com.zereao.wechat.commom.utils.encrypt.EncrypterFactory;
import com.zereao.wechat.data.vo.ApiTestVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author Zereao
 * @version 2018/11/03  21:18
 */
@Service
public class ApiTestService {
    public String checkParams(ApiTestVo apiTestVo) {
        String signature = apiTestVo.getSignature();
        String timestamp = apiTestVo.getTimestamp();
        Integer nonce = apiTestVo.getNonce();

        String token = "LOVE_BLUE_SKY";
        String[] tempStr = new String[]{token, signature, timestamp};
        Arrays.sort(tempStr);
        StringBuilder sb = new StringBuilder();
        for (String str : tempStr) {
            sb.append(str);
        }
        Encrypter sha1Encrypter = EncrypterFactory.getInstance(EncryptAlgorithmEnum.SHA1);
        String result = sha1Encrypter.encrypt(sb.toString());
        if (result.equals(signature)) {
            return apiTestVo.getEchostr();
        }
        return "false";
    }
}
