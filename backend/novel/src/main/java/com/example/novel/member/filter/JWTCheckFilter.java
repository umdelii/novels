package com.example.novel.member.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.novel.member.dto.MemberDTO;
import com.example.novel.member.utils.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    // jwt token 검사 제외 url 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // /api/novels + /api/novels/{id} + swagger 필터 x
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();
        log.trace("check url {}", path);

        if (path.startsWith("/swagger-ui") || (path.startsWith("/v3/api-docs"))
                || (path.startsWith("/api/members/login"))) {
            return true;
        }

        // \\d+$ : \d == 숫자 0-9, + == 1~무제한 , $ == 끝나는
        // if (path.startsWith("/api/novels") && request.getMethod().equals("GET")
        // || (path.matches("/api/novels/\\d+$"))) {
        // return true;
        // }

        // if (path.startsWith("/api/novels")) {
        // if (request.getMethod().equals("GET")) {
        // return true;
        // }
        // }

        if (path.startsWith("/api/novels") && (request.getMethod().equals("GET"))) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("------------------JWT FILTER------------------");

        // 클라이언트는 헤더를 통해서 토큰을 서버로 전송할 수 있다.
        String authHeaderStr = request.getHeader("Authorization");
        // token 값 시작 문자열 : Bearer (한칸 공백)
        if (authHeaderStr != null && authHeaderStr.startsWith("Bearer ")) {

            try {
                // Bearer 이후 문자 분리 index 7
                String accessToken = authHeaderStr.substring(7);
                // token 유효성 검증
                Map<String, Object> claims = JWTUtil.validateToken(accessToken);

                log.info("claims {}", claims);

                // claims 값 추출
                String email = (String) claims.get("email");
                String password = (String) claims.get("password");
                String nickname = (String) claims.get("nickname");
                Boolean social = (Boolean) claims.get("social");
                List<String> roles = (List<String>) claims.get("roles");

                MemberDTO memberDTO = new MemberDTO(email, password, nickname, social, roles);

                // 인증정보 => SecurityContext 저장
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        memberDTO,
                        password, memberDTO.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
            } catch (Exception e) {
                log.error("JWT token error");
                log.error(e.getMessage());

                // 에러메세지 전달(json 형태)
                Gson gson = new Gson();
                String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
                response.setContentType("application/json");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(msg);
                printWriter.close();
            }
        }

    }
}