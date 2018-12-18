package com.zereao.wechat.service.event;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.data.dto.NewsMessageDTO;
import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.data.vo.event.SubscribeEventVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 关注/取消关注事件
 *
 * @author Zereao
 * @version 2018/12/13  15:12
 */
@Slf4j
@Service
public class SubscribeEventService extends AbstractEventService {
    @Value("${welcom.msg.title}")
    private String title;
    @Value("${welcom.msg.banner}")
    private String bannerUrl;
    @Value("${welcom.msg.description}")
    private String description;
    @Value("${welcom.msg.url}")
    private String detail;

    @Override
    public Object handleEvent(ParentMsgVO parentVO) {
        SubscribeEventVO eventVO = new SubscribeEventVO();
        BeanUtils.copyProperties(parentVO, eventVO);
        NewsMessageDTO.Articles.Item item = NewsMessageDTO.Articles.Item.builder()
                .title(title).picUrl(bannerUrl).description(description).url(detail).build();
        NewsMessageDTO.Articles articles = NewsMessageDTO.Articles.builder().item(item).build();
        NewsMessageDTO response = NewsMessageDTO.builder().articleCount(1).articles(articles)
                .toUserName(eventVO.getFromUserName()).msgType(MsgType.NEWS)
                .fromUserName(eventVO.getToUserName()).createTime(new Date()).build();
        return response;
    }
}
