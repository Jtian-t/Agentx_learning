package com.jin.agentx_learning.infrastructure.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {

    // 这行代码的作用就是初始化一个属于 JwtUtils 类的日志记录工具.
    // 传入 JwtUtils.class 是为了告诉日志系统：“接下来的日志是谁打印的？” 这样在日志文件中，你就能清晰地看到每条信息对应的类名，方便排查定位问题。
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static String jwtSecret = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // token过期时间 - 24小时
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    //将一个 Base64 编码的字符串（原始密钥）转换成 Java 程序可以直接使用的、符合 HMAC 算法要求的密钥对象。
    private static SecretKey getSigningKey() {
        //jwtSecret: 这是一个通常存储在配置文件（如 application.yml）中的字符串。为了安全和传输方便，密钥通常会以 Base64 编码格式存储（例如：Y29uZmlnLWtleS1leGFtcGxl...）。
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        //转换成 HMAC 专用密钥
        //安全检查：它不仅是转换格式，还会检查你的密钥长度。如果你的 keyBytes 太短（例如低于 256 位/32 字节），这个方法会抛出异常，强制要求你使用足够强度的密钥。
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /** 生成JWT Token */
    public static String generateToken(String userId) {
        if (!StringUtils.hasText(userId)) {
            logger.error("生成JWT Token失败: 用户ID为空");
            throw new IllegalArgumentException("用户ID不能为空");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // 我jwt令牌里的载荷, 包含用户ID和过期时间
        String token = Jwts.builder().subject(userId).issuedAt(now).expiration(expiryDate).signWith(getSigningKey())
                .compact();
        // 旧版本使用的API,<dependency>
        //            <groupId>io.jsonwebtoken</groupId>
        //            <artifactId>jjwt-api</artifactId>
        //<!--  <version>0.11.5</version> 这个版本旧了,不符合Agentx使用的版本-->
        //            <version>${jjwt.version}</version>
        //        </dependency>
        //        String token = Jwts.builder().setSubject(userId).setIssuedAt(now).setExpiration(expiryDate).signWith(getSigningKey())
        //                .compact();

        logger.debug("成功生成JWT Token: 用户ID={}, 过期时间={}", userId, expiryDate);
        return token;
    }

    /** 从token中获取用户ID */
    public static String getUserIdFromToken(String token) {
        if (!StringUtils.hasText(token)) {
            logger.warn("获取用户ID失败: Token为空");
            return null;
        }

        try {
            Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();

            String userId = claims.getSubject();
            Date expiration = claims.getExpiration();

            logger.debug("成功解析Token: 用户ID={}, 过期时间={}", userId, expiration);
            return userId;

        } catch (JwtException e) {
            logger.warn("解析Token失败: {}, Token前缀: {}", e.getMessage(),
                    token.length() > 20 ? token.substring(0, 20) + "..." : token);
            return null;
        } catch (Exception e) {
            logger.error("解析Token异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /** 验证token是否有效 */
    public static boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            logger.debug("Token验证失败: Token为空");
            return false;
        }

        try {
            Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();

            // 检查是否过期
            Date expiration = claims.getExpiration();
            Date now = new Date();

            if (expiration.before(now)) {
                logger.warn("Token验证失败: Token已过期, 过期时间={}, 当前时间={}", expiration, now);
                return false;
            }

            logger.debug("Token验证成功: 用户ID={}, 剩余有效时间={}分钟", claims.getSubject(),
                    (expiration.getTime() - now.getTime()) / (1000 * 60));

            return true;

        } catch (JwtException e) {
            logger.warn("Token验证失败 - JWT异常: {}, Token前缀: {}", e.getMessage(),
                    token.length() > 20 ? token.substring(0, 20) + "..." : token);
            return false;
        } catch (Exception e) {
            logger.error("Token验证异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /** 检查Token是否即将过期（1小时内） */
    public static boolean isTokenExpiringSoon(String token) {
        if (!StringUtils.hasText(token)) {
            return true;
        }

        try {
            Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();

            Date expiration = claims.getExpiration();
            Date now = new Date();

            // 检查是否在1小时内过期
            long timeUntilExpiry = expiration.getTime() - now.getTime();
            boolean expiringSoon = timeUntilExpiry < (60 * 60 * 1000); // 1小时

            if (expiringSoon) {
                logger.info("Token即将过期: 用户ID={}, 剩余时间={}分钟", claims.getSubject(), timeUntilExpiry / (1000 * 60));
            }

            return expiringSoon;

        } catch (Exception e) {
            logger.debug("检查Token过期状态失败: {}", e.getMessage());
            return true;
        }
    }
}