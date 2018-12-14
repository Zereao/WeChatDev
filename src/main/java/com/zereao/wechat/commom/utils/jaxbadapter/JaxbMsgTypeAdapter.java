package com.zereao.wechat.commom.utils.jaxbadapter;

import com.zereao.wechat.commom.constant.MsgType;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Jaxb MsgType 适配器
 *
 * @author Zereao
 * @version 2018/12/12  15:05
 */
public class JaxbMsgTypeAdapter extends XmlAdapter<String, MsgType> {
    @Override
    public MsgType unmarshal(String v) throws Exception {
        return MsgType.of(v);
    }

    @Override
    public String marshal(MsgType v) throws Exception {
        return v.value();
    }
}
