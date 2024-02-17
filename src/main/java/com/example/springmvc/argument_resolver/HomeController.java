package com.example.springmvc.argument_resolver;

import com.example.springmvc.frontcontroller.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HomeController {
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {
        if (loginMember == null) {
            return "noMember";
        }

        return "ok";
    }
}
