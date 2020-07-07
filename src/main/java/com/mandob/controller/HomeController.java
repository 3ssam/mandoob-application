package com.mandob.controller;

import com.mandob.response.HomeCounters;
import com.mandob.service.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("home")
public class HomeController {

    @Autowired
    private final HomeService homeService;

    @GetMapping
    public HomeCounters getCounters() {
        return homeService.getCounters();
    }
}
