package com.zereao.wechat.service;

import com.zereao.wechat.commom.utils.encrypt.EncryptAlgorithmEnum;
import com.zereao.wechat.commom.utils.encrypt.Encrypter;
import com.zereao.wechat.commom.utils.encrypt.EncrypterFactory;
import com.zereao.wechat.data.vo.ApiTestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author Zereao
 * @version 2018/11/03  21:18
 */
@Slf4j
@Service
public class ApiTestService {
    public String checkParams(ApiTestVo apiTestVo) {
        String signature = apiTestVo.getSignature();
        String timestamp = apiTestVo.getTimestamp();
        Integer nonce = apiTestVo.getNonce();

        String token = "lovebluesky";
        String[] tempStr = new String[]{token, signature, timestamp};
        log.info(tempStr[0].concat(tempStr[1]).concat(tempStr[2]));
        Arrays.sort(tempStr);
        StringBuilder sb = new StringBuilder();
        for (String str : tempStr) {
            sb.append(str);
        }
        log.info("========================================================");
        log.info(sb.toString());
        String concatStr = tempStr[0].concat(tempStr[1]).concat(tempStr[2]);
        log.info(concatStr);
        log.info("========================================================");
        Encrypter sha1Encrypter = EncrypterFactory.getInstance(EncryptAlgorithmEnum.SHA1);
        String result = sha1Encrypter.encrypt(sb.toString());
        String theStr = sha1Encrypter.encrypt(concatStr);
        log.debug("signature = {}", signature);
        log.debug("encryptStringBuilderStr = {}", result);
        log.debug("encryptConcatStr = {}", theStr);
        if (result.equals(signature)) {
            return apiTestVo.getEchostr();
        }
        return "false";
    }
}
