package com.example.springmvc.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/springmvc/members")
public class SpringMemberFormControllerV1 {
    @GetMapping("/new-form")
    public String process(@RequestParam("username") String username){
        return "new-form";
    }
}
