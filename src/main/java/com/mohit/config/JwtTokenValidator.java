package com.mohit.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(JwtConstant.JWT_TOKEN_HEADER);
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            try{
                jwtToken = jwtToken.substring(7);
                SecretKey key= Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET_KEY.getBytes());
                Claims claim=Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();

                String username=String.valueOf(claim.get("username"));
                String authorities=String.valueOf(claim.get("authorities"));

                List<GrantedAuthority> grantedAuthorities= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, grantedAuthorities));
            }catch (Exception e){
                throw new BadCredentialsException("Invalid JWT token");
            }
            filterChain.doFilter(request, response);
        }
    }
}
