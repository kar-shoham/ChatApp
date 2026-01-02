package com.shoham.chat_app.utils;

import com.shoham.chat_app.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtils
{
    private SecretKey key;

    JwtUtils(@NonNull @Value("${jwt.secret-key}") String jwtSecret)
    {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getToken(@NonNull User user)
    {
        return Jwts.builder()
                .subject(user.getUsername()).signWith(key).compact();
    }

    public String getUsername(@NonNull String token)
    {
        return Jwts.parser()
                .verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
