package com.zereao.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zereao
 * @version 2018/12/14  19:02
 */
@Controller
@RequestMapping("/wel")
public class WelcomeController {
    @RequestMapping("/a")
    public String welcome() {
        return "welcome/index";
    }
}
