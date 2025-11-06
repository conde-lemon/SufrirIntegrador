package com.travel4u.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DiagnosticPageController {

    @GetMapping("/diagnostic")
    public String diagnosticPage() {
        return "diagnostic";
    }
}