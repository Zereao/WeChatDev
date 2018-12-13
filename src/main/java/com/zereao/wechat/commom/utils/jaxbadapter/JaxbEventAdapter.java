package com.zereao.wechat.commom.utils.jaxbadapter;

import com.zereao.wechat.commom.constant.Event;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Jaxb Event 适配器
 *
 * @author Zereao
 * @version 2018/12/12  15:05
 */
public class JaxbEventAdapter extends XmlAdapter<String, Event> {
    @Override
    public Event unmarshal(String v) throws Exception {
        return Event.of(v);
    }

    @Override
    public String marshal(Event v) throws Exception {
        return v.value();
    }
}
