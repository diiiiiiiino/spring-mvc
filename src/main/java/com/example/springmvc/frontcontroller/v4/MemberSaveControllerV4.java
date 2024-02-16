package com.example.springmvc.frontcontroller.v4;

import com.example.springmvc.frontcontroller.Member;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4{
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String userName = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(userName, age);

        model.put("member", member);

        return "save-result";
    }
}
