package com.webapp.HotelWebAppBE.Security.jwt;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.webapp.HotelWebAppBE.Service.CustomerDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseCookie;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//GOOGLE
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${hotelweb.app.jwtSecret}")
    private String jwtSecret;

    @Value("${hotelweb.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${hotelweb.app.jwtCookieName}")
    private String jwtCookie;

    @Value("google.client-id")
    private String googleClientId;


    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String cookieValue = request.getHeader("Authorization");
        return cookieValue;
    }

    public ResponseCookie generateJwtCookie(CustomerDetailsImpl customerService) {
        //String jwt = generateTokenFromUsername(customerService.getUsername());
        String jwt = generateTokenFromUsername(customerService);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public boolean validateGoogleJWTToken(String authToken){
        GoogleIdTokenVerifier verifier =null;
        GoogleIdToken idToken = null;
        HttpTransport transport = new HttpTransport() {
            @Override
            protected LowLevelHttpRequest buildRequest(String s, String s1) throws IOException {
                return null;
            }
        };
        JsonFactory jsonFactory = new JsonFactory() {
            @Override
            public JsonParser createJsonParser(InputStream inputStream) throws IOException {
                return null;
            }

            @Override
            public JsonParser createJsonParser(InputStream inputStream, Charset charset) throws IOException {
                return null;
            }

            @Override
            public JsonParser createJsonParser(String s) throws IOException {
                return null;
            }

            @Override
            public JsonParser createJsonParser(Reader reader) throws IOException {
                return null;
            }

            @Override
            public JsonGenerator createJsonGenerator(OutputStream outputStream, Charset charset) throws IOException {
                return null;
            }

            @Override
            public JsonGenerator createJsonGenerator(Writer writer) throws IOException {
                return null;
            }
        };
        try {
            verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();
            idToken = verifier.verify(authToken);
            if(idToken == null)
                return false;
            else
                return true;
        }catch (Exception e){
            System.out.println("Invalida token");
            return false;
        }

    }

//    public String generateTokenFromUsername(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
//    }

    public String generateTokenFromUsername(CustomerDetailsImpl customerDetails) {
        Map<String,Object> claims= new HashMap<>();
        claims.put("email", customerDetails.getEmail());
        claims.put("name",customerDetails.getName());
        return Jwts.builder()
                .setSubject(customerDetails.getUsername())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
}
