package com.example.ecommerceProject.security;

import com.example.ecommerceProject.model.tokenStore.RegisterToken;
import com.example.ecommerceProject.repository.RegisterTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    @Autowired
    RegisterTokenRepo registerTokenRepo;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public Boolean isTokenTimeExpired(String token) {
        RegisterToken registerToken = registerTokenRepo.getRegisterTokenByUuidToken(token);

        LocalDateTime expirationDate = registerToken.getValid();
        System.out.println(expirationDate);

        // Calculate the difference between the expiration date and the current date in milliseconds
        long timeDiff = LocalDateTime.now().getMinute() - expirationDate.getMinute();
        System.out.println(timeDiff);

        // If the time difference is greater than 0, the token is still valid
        // Otherwise, the token has expired
        return timeDiff > 0;


    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public String generateToken(String email, Long time) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, time);
    }

    private String createToken(Map<String, Object> claims, String email, Long time) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 6 * 60 * time))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
