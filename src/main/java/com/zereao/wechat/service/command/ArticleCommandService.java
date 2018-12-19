package com.zereao.wechat.service.command;

import com.zereao.wechat.commom.constant.Command;
import com.zereao.wechat.data.vo.ParentMsgVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Zereao
 * @version 2018/12/19  20:33
 */
@Slf4j
@Service
public class ArticleCommandService extends AbstractCommandService {
    @Override
    public Object exec(Command command, ParentMsgVO msgVO) {
        log.info("----------------->    GetAllArticleListCommandService.exec() 执行！ command = {} ", command);
        return null;
    }
}
