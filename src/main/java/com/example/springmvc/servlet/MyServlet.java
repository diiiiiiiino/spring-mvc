package com.example.springmvc.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "MyServlet", value = "/hello")
public class MyServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] usernames = request.getParameterValues("username");
        String username = request.getParameter("username");
        System.out.println("username = " + username);
        System.out.println("usernames[0] == username : " + username.equals(usernames[0]));

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello " + username);
    }
}
