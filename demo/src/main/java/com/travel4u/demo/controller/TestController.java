package com.travel4u.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    @ResponseBody
    public String testGet() {
        return "GET funciona correctamente";
    }

    @PostMapping("/post")
    @ResponseBody
    public String testPost() {
        return "POST funciona correctamente";
    }

    @GetMapping("/form")
    public String showTestForm() {
        return "test-form";
    }

    @GetMapping("/header")
    public String showHeaderTest() {
        return "header_test";
    }

    @GetMapping("/servicedesk")
    public String showServiceDeskTest() {
        return "servicedesk_test";
    }
}