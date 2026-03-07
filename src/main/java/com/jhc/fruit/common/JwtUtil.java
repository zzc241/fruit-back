package com.jhc.fruit.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static String signKey = "dsj2024101010signKey0must0be0at0least0320chars";
    private static Long expire = 43200000L;

    //生成JWT令牌

    public static String generateJwt(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    //解析JWT令牌
    public static Claims parseJwt(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}