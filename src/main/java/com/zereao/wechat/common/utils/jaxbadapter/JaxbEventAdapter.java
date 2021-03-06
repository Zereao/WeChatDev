package com.zereao.wechat.common.utils.jaxbadapter;

import com.zereao.wechat.common.constant.Event;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Jaxb Event 适配器
 *
 * @author Darion Mograine H
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
