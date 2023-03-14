package com.example.springbootdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/test")
public class Test {

    @RequestMapping(value="/hello")
    @ResponseBody
    public String hello(){
        return "nice";
//        System.out.println("hello");
    }

}
