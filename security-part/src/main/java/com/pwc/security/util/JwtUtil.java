package com.pwc.security.util;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成jwtToken
     */
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 计算过期时间
     */
    private Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String userName = claims.getSubject();
        return userName;
    }

    /**
     * 验证token是过期
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String userName = getUsernameFromToken(token);
        return userName.equals(userDetails.getUsername()) && !isExpired(token);
    }

    /**
     * 判断token是否失效
     */
    private boolean isExpired(String token){
        Date expireDate = getExpireDateFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * 获取token中的过期时间
     */
    private Date getExpireDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateTokenByUserDetails(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 刷新token过期时间
     */
    public String refreshToken(String oldToken) {
        if(StrUtil.isEmpty(oldToken)){
            return null;
        }

        String newToken = oldToken.substring(tokenHead.length());
        if(StrUtil.isEmpty(newToken)){
            return null;
        }
//        LOGGER.debug("==============> oldToken: {}, newToken: {}", oldToken, newToken);
        Claims claims = getClaimsFromToken(newToken);
        if(claims == null){
            return null;
        }

        if(isExpired(newToken)){
            return null;
        }

        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
