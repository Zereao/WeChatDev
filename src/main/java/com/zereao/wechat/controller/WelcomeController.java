package com.zereao.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zereao
 * @version 2018/12/14  19:02
 */
@Controller
public class WelcomeController {
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome/welcome";
    }
}
