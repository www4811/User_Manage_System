package com.example.user_manage_system.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil ;
    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response , Object handler )throws  Exception{
      String authHeader=request.getHeader("Authorization");
      if(authHeader==null || !authHeader.startsWith("Bearer ")){
          response.setStatus(401);
          return false;
      }String token=authHeader.substring(7);
      try{
          Long userId=jwtUtil.getUserIdFromToken(token);
          String role=jwtUtil.getRoleFromToken(token);
          request.setAttribute("userId", userId);
          request.setAttribute("role", role);
          return true;
      }catch(Exception e){
          response.setStatus(401);
          return false;
      }
    }
}
