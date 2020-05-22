package com.lin.missyou.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtToken {

    private static String jwtkey;
    private static Integer expiredTimeIn;
    private static Integer defaultScope = 8;

    @Value("${missyou.security.jwt-key}")
    public void setJwtkey(String jwtkey) {
        JwtToken.jwtkey = jwtkey;
    }

    @Value("${missyou.security.token-expired-in}")
    public void setExpiredTimeIn(Integer expiredTimeIn) {
        JwtToken.expiredTimeIn = expiredTimeIn;
    }

    public static String makeToken(Long uid, Integer scope) { //scope为用户权限登记
        return JwtToken.getToken(uid, scope);
    }

    public static String makeToken(Long uid) {
        return JwtToken.makeToken(uid, JwtToken.defaultScope);
    }

    private static String getToken(Long uid, Integer scope) {
        //指定算法
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtkey);
        //计算token颁布时间和过期时间
        Map<String, Date> dateMap = JwtToken.calculateExpiredIssues();
        //生成token
        String token = JWT.create()
                .withExpiresAt(dateMap.get("expiredTime")) //必须添加
                .withIssuedAt(dateMap.get("now")) //必须添加
                .withClaim("uid", uid) //自定义添加
                .withClaim("scope", scope) //自定义添加
                .sign(algorithm);
        return token;
    }

    public static Optional<Map<String, Claim>> getClaims(String token) {
        DecodedJWT decodedJWT = null;
        //算法
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtkey);
        //token校验类
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            decodedJWT = jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            return Optional.empty(); //非法或过期
        }
        return Optional.of(decodedJWT.getClaims());
    }

    public static Boolean verifyToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtkey); //算法
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
        } catch (JWTVerificationException e){
            return false;
        }
        return true;
    }

    private static Map<String, Date> calculateExpiredIssues() {
        Map<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        map.put("now", now);
        calendar.add(Calendar.SECOND, JwtToken.expiredTimeIn);
        map.put("expiredTime", calendar.getTime());
        return map;
    }
}
