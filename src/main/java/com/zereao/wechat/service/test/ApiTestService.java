package com.zereao.wechat.service.test;

import com.zereao.wechat.data.vo.test.ApiTestVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zereao
 * @version 2018/11/03  21:18
 */
@Slf4j
@Service
public class ApiTestService {
    @Value("${wechat.token}")
    private String token;

    public String checkParams(ApiTestVO apiTestVo) {
        String resultStr = Stream.of(token, apiTestVo.getTimestamp(), apiTestVo.getNonce()).sorted().collect(Collectors.joining());
        String encryptedStr = DigestUtils.sha1Hex(resultStr);
        if (encryptedStr.equals(apiTestVo.getSignature())) {
            return apiTestVo.getEchostr();
        }
        return "FAILED";
    }
}
