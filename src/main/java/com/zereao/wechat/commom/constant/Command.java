package com.zereao.wechat.commom.constant;


import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.command.AbstractCommandService;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Zereao
 * @version 2018/12/19  20:07
 */
public enum Command {
    /**
     * 获取所有文章列表的命令
     */
    GET_ALL_ARTICLE_LIST("1") {
        @Override
        public Object exec(AbstractCommandService commandService, ParentMsgVO msgVO) {
            return commandService.exec(this, msgVO);
        }
    },
    GET_ARTICLE("1-1") {
        @Override
        public Object exec(AbstractCommandService commandService, ParentMsgVO msgVO) {
            return commandService.exec(this, msgVO);
        }
    };

    private String commandCode;

    Command(String commandCode) {
        this.commandCode = commandCode;
    }

    public String code() {
        return this.commandCode;
    }

    public Command of(String code) {
        return Arrays.stream(Command.values()).filter(c -> c.code().equals(code)).findFirst().orElse(null);
    }

    public abstract Object exec(AbstractCommandService commandService, ParentMsgVO msgVO);
}
