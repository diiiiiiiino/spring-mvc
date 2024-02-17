package com.example.springmvc.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/session")
@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionManager sessionManager;

    @PostMapping
    public String createSession(HttpServletResponse response) {
        sessionManager.createSession("1", response);
        return "ok";
    }

    @GetMapping
    public String getSession(HttpServletRequest request) {
        String memberId = (String) sessionManager.getSession(request);

        log.info("memberId : " + memberId);

        return "ok";
    }

    @DeleteMapping
    public String deleteSession(HttpServletRequest request) {
        sessionManager.expire(request);

        return "ok";
    }
}
