package com.monitor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: dingxh
 * @description:
 * @create: 16:53
 **/
@RestController
public class TestController {
    @RequestMapping(value = "/")
    public String welcome(){
        return "hello monitor";
    }
}