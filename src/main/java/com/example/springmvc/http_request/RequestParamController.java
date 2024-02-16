package com.example.springmvc.http_request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {
    @ResponseBody
    @RequestMapping("/request-param")
    public String requestParam(@RequestParam("username") String username,
                               @RequestParam("age") int memberAge){

        log.info("username={}, age={}", username, memberAge);
        return "ok";
    }

    /**
     * @RequestParam 생략 시 MVC 내부에서 required=false 적용
     */
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(String username, int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * ?username= 파라미터 이름만 있고 값이 없는 경우 -> 빈 문자로 통과됨
     * @RequestParam(required = false) int age null 입력 시 -> 500 예외 발생
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(required = true) String username,
                               @RequestParam(required = false) Integer age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 빈 문자의 경우에도 적용
     * /request-param-default?username=
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(@RequestParam(required = true, defaultValue = "guest") String username,
                               @RequestParam(required = false, defaultValue = "-1") int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     *  파라미터 값이 1개면 Map, 여러개면 MultiValueMap
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamDefault(@RequestParam Map<String, Object> paramMap){
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
}
