package com.example.springmvc.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "responseJsonServlet", value = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * //response.setCharacterEncoding("utf-8");
     * response.getWriter().write(result);
     * -> application/json;charset=ISO-8859-1
     *
     * response.getOutputStream().write(result.getBytes());
     * -> application/json
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "application/json");

        HelloData data = new HelloData();
        data.setUserName("홍길동");
        data.setAge(20);

        String result = objectMapper.writeValueAsString(data);

        response.getOutputStream().write(result.getBytes());
    }
}
