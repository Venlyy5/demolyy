package com.dfds.demolyy.WebsocketDemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websocket")
public class InitController {

    @GetMapping("/testLyy")
    public String init() {
        return "websocket.html";
    }

}