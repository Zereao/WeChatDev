package com.zereao.wechat.commom.constant;


import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.command.AbstractCommandService;

import java.util.Arrays;

/**
 * @author Zereao
 * @version 2018/12/19  20:07
 */
public enum Command {
    /**
     * 获取所有文章列表的命令
     */
    GET_ALL_ARTICLE_LIST("1"),
    GET_ARTICLE("1-1"),

    COMMAND_NOT_EXISTS("");

    /**
     * 命令码
     */
    private String code;

    Command(String code) {
        this.code = code;
    }

    /**
     * 获取当前命令的命令码
     *
     * @return 当前命令的命令码
     */
    public String code() {
        return this.code;
    }

    /**
     * 根据code查找命令，如果命令不存在，则返回 COMMAND_NOT_EXISTS 命令
     *
     * @param code 命令码
     * @return 命令
     */
    public Command of(String code) {
        return Arrays.stream(Command.values()).filter(c -> c.code().equals(code)).findFirst().orElse(COMMAND_NOT_EXISTS);
    }

    public Object exec(AbstractCommandService commandService, ParentMsgVO msgVO) {
        return commandService.exec(this, msgVO);
    }
}
