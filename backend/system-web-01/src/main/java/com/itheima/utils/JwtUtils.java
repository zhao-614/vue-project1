package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 🔒 敏感信息：JWT签名密钥，生产环境请使用环境变量 JWT_SIGN_KEY 替代
    // private static final String signKey = "emhhb19qd3Rfc2VjcmV0X2tleV8yMDI0X3NlY3VyZSE=";
    private static final String signKey = System.getenv().getOrDefault("JWT_SIGN_KEY", "your-secret-key-at-least-32-characters!!");
    private static final Long expire =  12 * 60 * 60 * 1000L;
    /**
     * 生成JWT令牌
     * @param claims 自定义声明
     * @return JWT令牌
     */
    public static String generateJwt(Map<String,Object> claims){
        // 创建密钥
        SecretKey key = Keys.hmacShaKeyFor(signKey.getBytes());
        
        String jwt = Jwts.builder()
                .claims(claims)  //编码时添加自定义声明
                .expiration(new Date(System.currentTimeMillis() + expire))  // 设置令牌有效期
                .signWith(key)  // 签名（包含了密钥和签名算法）
                .compact();// 生成令牌
        return jwt;
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public static Claims parseJWT(String jwt){
        // 创建密钥
        SecretKey key = Keys.hmacShaKeyFor(signKey.getBytes());
        
        return Jwts.parser()
                .verifyWith(key)  // 验证（包含了密钥和签名算法）
                .build()// 创建解析器
                .parseSignedClaims(jwt)  // 解析令牌
                .getPayload();// 获取负载（第二部分）
    }
}
