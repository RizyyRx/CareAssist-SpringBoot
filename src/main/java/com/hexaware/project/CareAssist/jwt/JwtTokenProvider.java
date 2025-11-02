package com.hexaware.project.CareAssist.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
    private String jwtSecret;
 
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;
 
    // generate JWT token
    public String generateToken(Authentication authentication){
 
        String username = authentication.getName();
        
        String role = authentication.getAuthorities().iterator().next().getAuthority();
 
        Date currentDate = new Date();
 
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
 
        String token = Jwts.builder()
        	    .setSubject(username)
        	    .claim("role", role)
        	    .setIssuedAt(new Date())
        	    .setExpiration(expireDate)
        	    .signWith(key())
        	    .compact();
 
        return token;
    }
 
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
 
    // get username from JWT token
    public String getUsername(String token){
 
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
 
    // validate JWT token
    public boolean validateToken(String token){
        Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token);
        return true;
 
    }
}
