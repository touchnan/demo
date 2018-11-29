package cn.touch.demo.boot.demospringboot.jwt;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengqiang.han on 2018/11/29.
 */
public class JwtAuth {
    public static void main(String[] args) {

        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DATE, 10);
        Date expiresDate = nowTime.getTime();


        String SECRET = "woshiyigeren";

        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        String token = JWT.create().withHeader(header)
                .withAudience("what", "a", "b")
                .withClaim("uid", "007007")
                .withClaim("nickname", "hcq")
//                .withIssuedAt(iatDate)
//                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(SECRET));

        System.out.println(token);

        JWTVerifier build = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT verify = build.verify(token);
        System.out.println("verify = " + JSON.toJSONString(verify));

        Map<String, Claim> claims = verify.getClaims();
        System.out.println("claims = " + JSON.toJSONString(claims));

        System.out.println("audience = " + JSON.toJSONString(verify.getAudience()));
        Claim uid = claims.get("uid");
        System.out.println("uid = " + uid.asString());
        System.out.println("nickname = " + claims.get("nickname").asString());
        System.out.println("aud = " + claims.get("aud").asString());


    }
}
