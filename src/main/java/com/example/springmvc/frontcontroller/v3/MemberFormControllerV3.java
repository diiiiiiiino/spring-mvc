package com.example.springmvc.frontcontroller.v3;

import com.example.springmvc.frontcontroller.ModelView;
import com.example.springmvc.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberFormControllerV3 implements ControllerV3 {
    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }
}
