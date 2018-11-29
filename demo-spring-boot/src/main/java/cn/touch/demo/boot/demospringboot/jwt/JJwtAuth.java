package cn.touch.demo.boot.demospringboot.jwt;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by chengqiang.han on 2018/11/29.
 */
public class JJwtAuth {
    public static void main(String[] args) {
        String SECRET = "woshiyigeren";

        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        //有10天有效期
        nowTime.add(Calendar.DATE, 10);
        Date expiresDate = nowTime.getTime();

        Claims claims = Jwts.claims();
        claims.put("name","hcq");
        claims.put("uid", "007007");
        claims.setAudience("cy");
        claims.setIssuer("cy");
        String token = Jwts.builder().setClaims(claims).setExpiration(expiresDate)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        System.out.println("token = " + token);


        Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        String signature = jws.getSignature();
        Map<String, String> header = jws.getHeader();
        System.out.println("header = " + JSON.toJSONString(header));
        Claims Claims = jws.getBody();
        System.out.println("Claims = " + JSON.toJSONString(Claims));

    }
}
