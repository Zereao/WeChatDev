package com.zereao.wechat.commom.constant;


import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.command.AbstractCommandService;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Zereao
 * @version 2018/12/19  20:07
 */
public enum Command {
    /**
     * 获取所有文章列表的命令
     */
    GET_ALL_ARTICLES("1"),
    /**
     * 获取某一条文章信息
     */
    GET_ARTICLE("1-*"),
    /**
     * 获取某一条文章信息
     */
    ADD_ARTICLE("1-root.add"),
    /**
     * 用户发送的消息非定义了的命令
     */
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
     * 根据code查找符合正则的命令，如果命令不存在，则返回 COMMAND_NOT_EXISTS 命令
     *
     * @param code 命令码
     * @return 命令
     */
    public static Command of(String code) {
        /* \d 表示 匹配一个数字字符
         * + 表示匹配 + 加号前面的字符串一次或多次，至少一次。例如，'zo+' 能匹配 "zo" 以及 "zoo"，但不能匹配 "z"。
         * $ 匹配输入字符串的结束位置
         * 整个正则的意思是，在结束位置前，匹配一个 -数字 这个格式的数据一次或多次，这个数据后面必须是结束位置
         */
        return Arrays.stream(Command.values()).filter(command -> command.code().startsWith(code.replaceAll("-\\d+$", "-"))).findFirst().orElse(COMMAND_NOT_EXISTS);
    }

    public Object exec(Map<String, AbstractCommandService> commandServiceMap, ParentMsgVO msgVO) {
        return this.getBean(commandServiceMap).exec(this, msgVO);
    }

    public AbstractCommandService getBean(Map<String, AbstractCommandService> commandServiceMap) {
        if (this.code.startsWith("1")) {
            return commandServiceMap.get("articleCommandService");
        } else {
            return commandServiceMap.get("abstractCommandService");
        }
    }
}
