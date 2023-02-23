package com.dfds.demolyy.utils.otherUtils;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

/**
 * JWT 工具类
 */
public class JwtUtils {

    // 加密KEY
    private static final String TOKEN_ENCRY_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";

    /**
     * 加密token
     */
    public static String createToken(Map<String, Object> claimMaps,int expire) {
        return Jwts.builder()
                .addClaims(claimMaps)
                .setExpiration(DateTime.now().offset(DateField.MINUTE,expire).toJdkDate())
                .signWith(SignatureAlgorithm.HS256, generalKey())
                .compact();
    }

    /**
     * 由字符串生成加密key
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(TOKEN_ENCRY_KEY.getBytes());
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    public static Boolean verifyToken(String token) {
        if(StringUtils.isBlank(token)) {
            return false;
        }
        try {
            parserToken(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析token
     */
    public static Jws<Claims> parserToken(String token) {
        if(token.contains("Bearer")) {
            token = token.replaceAll("Bearer ","");
        }
        return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token);
    }
    /**
     * 获取token中的用户信息
     */
    public static JSONObject getInfoFromToken(String token) {
        Jws<Claims> claimsJws = parserToken(token);
        Claims body = claimsJws.getBody();
        JSONObject authInfo = new JSONObject();
        authInfo.put("userId", body.get("userId", Long.class));
        authInfo.put("companyId", body.get("companyId", Long.class));
        authInfo.put("companyName", body.get("companyName", String.class));
        return authInfo;
    }
}