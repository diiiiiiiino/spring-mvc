package com.example.springmvc.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RequestMapping("/http-session")
@RestController
@RequiredArgsConstructor
public class HttpSessionController {

    @PostMapping
    public String createSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.setAttribute("memberId", "1");

        return "ok";
    }

    @GetMapping
    public String getSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null){
            return "noSession";
        }

        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));
        log.info("sessionId={}", session.getId());
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());

        return "ok";
    }

    @GetMapping("/attr")
    public String getSessionWithSessionAttribute(@SessionAttribute(value = "memberId", required = false) String memberId) {
        if(memberId == null){
            return "noSession";
        }

        return "ok";
    }

    @DeleteMapping
    public String deleteSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null){
            session.invalidate();
        }

        return "ok";
    }
}
