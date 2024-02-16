package com.example.springmvc.frontcontroller.v3;

import com.example.springmvc.frontcontroller.Member;
import com.example.springmvc.frontcontroller.ModelView;
import com.example.springmvc.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {
    @Override
    public ModelView process(Map<String, String> paramMap) {
        String userName = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(userName, age);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);

        return mv;
    }
}
