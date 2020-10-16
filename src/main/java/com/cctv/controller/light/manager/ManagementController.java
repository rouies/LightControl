package com.cctv.controller.light.manager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller")
public class ManagementController {

    @GetMapping("test")
    @ResponseBody
    public String test(){
        return "hello";
    }
}
