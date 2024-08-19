package com.elderlink.backend.auth.services.impl;

import com.elderlink.backend.auth.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY="d419ce37778265923e615c226b69adbf34c3707f20fa94703bd16dfbd5f52fca";
    private static final long accessTokenExpirationTime = 10000 * 60 * 300;
    /**
     * Extracts the username from the JWT token.
     *
     * @param jwtToken The JWT token
     * @return The extracted username
     */
    @Override
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }
    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token          The JWT token
     * @param claimsResolver Function to resolve the claim
     * @param <T>            Type of the claim value
     * @return The extracted claim value
     */


    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails The user details
     * @return The generated JWT token
     */
    @Override
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        //If we want to put extra claims
        //claims.put("userType", userDetails.getAuthorities());
        return buildToken(claims, userDetails.getUsername());
    }
    /**
     * Builds a JWT token with the specified claims and subject.
     *
     * @param extraClaims Additional claims to include in the token
     * @param subject     The subject (username) of the token
     * @return The generated JWT token
     */
    @Override
    public String buildToken(Map<String,Object> extraClaims, String subject){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Checks if the given JWT token is valid for the specified user details.
     *
     * @param token       The JWT token
     * @param userDetails The user details
     * @return True if the token is valid, otherwise false
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    /**
     * Checks if the given JWT token has expired.
     *
     * @param token The JWT token
     * @return True if the token has expired, otherwise false
     */
    @Override
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token
     * @return The expiration date
     */
    @Override
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    /**
     * Retrieves the signing key for JWT validation.
     *
     * @return The signing key
     */
    @Override
    public Key getSigningKey() {
        byte[] KeyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

}
