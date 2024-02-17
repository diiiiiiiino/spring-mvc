package com.example.springmvc.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/cookie")
@RestController
public class CookieController {

    @PostMapping
    public String createCookie(HttpServletResponse response) {
        //쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", "2");
        response.addCookie(idCookie);
        return "ok";
    }

    @GetMapping
    public String getCookie(@CookieValue(name = "memberId", required = false) Long memberId) {
        if(memberId == null){
            return "no cookie";
        }

        return "ok";
    }

    @DeleteMapping
    public String deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "ok";
    }
}
