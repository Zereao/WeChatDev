package com.zereao.wechat.common.utils.jaxbadapter;

import com.zereao.wechat.common.constant.MsgType;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Jaxb MsgType 适配器
 *
 * @author Darion Mograine H
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
