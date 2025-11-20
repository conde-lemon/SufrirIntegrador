package com.travel4u.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScraperTestController {

    @GetMapping("/scraper-test")
    public String getScraperTestPage() {
        return "scraper-test";
    }
}