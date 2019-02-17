package com.zereao.wechat.common.utils.jaxbadapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * Jaxb 日期适配器
 *
 * @author Darion Mograine H
 * @version 2018/12/11  19:24
 */
public class JaxbDateAdapter extends XmlAdapter<Long, Date> {
    @Override
    public Date unmarshal(Long v) throws Exception {
        return new Date(v * 1000L);
    }

    @Override
    public Long marshal(Date v) throws Exception {
        return v.getTime() / 1000;
    }
}
