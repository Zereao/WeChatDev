package com.zereao.wechat.commom.utils.jaxbadapter;

import com.zereao.wechat.commom.constant.MsgType;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * Jaxb MsgType 适配器
 *
 * @author Zereao
 * @version 2018/12/12  15:05
 */
public class JaxbMsgTypeAdapter extends XmlAdapter<MsgType, String> {
    @Override
    public String unmarshal(MsgType v) throws Exception {
        return v.value();
    }

    @Override
    public MsgType marshal(String v) throws Exception {
        return MsgType.valueOf(v);
    }
}
