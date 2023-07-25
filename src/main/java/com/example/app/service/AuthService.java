package com.example.app.service;

import com.example.app.dto.Token;
import com.example.app.model.AppUser;
import com.example.app.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class AuthService {

    static final private String JWT_SECRET_KEY = "FkYeL6o7zjdvr8+NW/Ega/mhbQcU2hzSujBvbFwZcegGZp7BeqvpE7fexXZRxsG2vAVcyKqJUu1pqBWF3Ys91d";

    static final private Long JWT_EXPIRY_MINUTES = 86400000L;

    @Autowired
    private UserRepository userRepository;

    public Token generateJwtToken(AppUser user) {
        String accessToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRY_MINUTES))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        return new Token(accessToken, JWT_EXPIRY_MINUTES);
    }

    public Boolean validateToken(String token) {
        Claims claims = getClaims(token);
        return userRepository.existsByEmail(claims.getSubject()) && claims.getExpiration().after(new Date());
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public AppUser getCurrentLoggedUser(String token) {
        String email = getClaims(token).getSubject();
        return userRepository.findByEmail(email);
    }

    private Key getSigningKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
