package com.shoham.chat_app.filter;

import com.shoham.chat_app.exception.InvalidTokenException;
import com.shoham.chat_app.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CustomJwtFilter
        extends OncePerRequestFilter
{

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException
    {
        String requestPath = request.getRequestURI();
        if(requestPath.equals("/api/v1/login") || requestPath.equals("/api/v1/signup")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = request.getHeader("Authorization");
            if(!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
                throw new InvalidTokenException("Invalid token!");
            }
            token = token.split("Bearer ")[1];

            String username = jwtUtils.getUsername(token);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, List.of()));
            filterChain.doFilter(request, response);

        } catch (InvalidTokenException ex) {
            handleInvalidTokenException(request, response, ex);
        }
    }

    private void handleInvalidTokenException(
            HttpServletRequest request,
            HttpServletResponse response,
            InvalidTokenException ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String jsonResponse = String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                timestamp,
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
    }
}
