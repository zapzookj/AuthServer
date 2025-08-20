package com.spring.authserver.security;

import com.spring.authserver.dto.ErrorResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Jws<Claims> jws = tokenProvider.parse(token);
                Claims claims = jws.getBody();
                String sub = claims.getSubject();
                String username = (String) claims.get("username");
                String roles = (String) claims.get("roles");
                List<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(","))
                    .filter(s -> !s.isBlank())
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toList());
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String json = toJson(new ErrorResponseDto(new ErrorResponseDto.ErrorBody(
                    "INVALID_TOKEN",
                    "유효하지 않은 인증 토큰입니다."
                )));
                response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String toJson(ErrorResponseDto responseDto) throws IOException {
        return "{\"error\":{\"code\":\"" + responseDto.getError().getCode() + "\",\"message\":\"" + responseDto.getError().getCode() + "\"}}";
    }
}
