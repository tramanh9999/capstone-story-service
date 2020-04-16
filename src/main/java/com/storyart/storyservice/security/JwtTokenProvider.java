package com.storyart.storyservice.security;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

//todo component scan qua day hay ko?
//todo ky hieu ::
@Component
public class JwtTokenProvider implements Serializable {


    @Value("${jwt.secret}")
    private String jwtSecret;


    @Value("${jwt.expirationInMs}")
    private int jwtExpirationInMs;


    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal)
                authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Integer.toString(userPrincipal.getId()))
                .claim("username", userPrincipal.getUsername())
                .claim("name", userPrincipal.getName())
                .claim("role", userPrincipal.getAuthorities().toArray()[0].toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }


    /*when user pass login , attach a token generated (server gave it from his username and password) into header
    , sending that token to access any resouce he can access by his role */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
