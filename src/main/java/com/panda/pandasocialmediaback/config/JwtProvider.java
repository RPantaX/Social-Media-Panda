package com.panda.pandasocialmediaback.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtProvider{

    @Value("classpath:key/private_key.pem")
    private Resource privateKeyResource;
    public String generateToken(Authentication auth) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email", auth.getName())
                .signWith(getSignKey(privateKeyResource), SignatureAlgorithm.RS256)
                .compact();
    }

    public String getEmailFromToken(String jwt) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        jwt = jwt.substring(7);
        return String.valueOf(getClaims(jwt).get("email"));
    }

    public boolean validateToken(String token, UserDetails userDetails) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final String username = getSubject(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public String getSubject(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return extractClaims(token, Claims::getSubject);
    }
    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims getClaims(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts.parserBuilder().setSigningKey(getSignKey(privateKeyResource)).build().parseClaimsJws(token).getBody();
    }
    private boolean isTokenExpired(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
    public PrivateKey getSignKey(Resource resource) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes= Files.readAllBytes(Paths.get(resource.getURI())); //obtenemos la ruta de la llave publica.
        String privateKeyPEM = new String(keyBytes, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "") //para que lea solo la parte codificada
                .replaceAll("\\s", ""); // "\\s" elimina los espacios en blanco

        byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);//decodificamos lo que queda dentro del string, en base 64 porque nuestra privateKey esta en base 64
        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    }
}
