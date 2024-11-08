package com.curso.diccionarios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Ni tiene sentido que este en el fichero este... ni que sea tan simple
    // Usa una clave secreta en base64 para crear la clave criptográfica
    SecretKey key = Jwts.SIG.HS256.key().build();


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

    public Claims extractAllClaims(String token) {
        return  Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // POdria sobrecargar esta función para que admita más datos adicionales que meter al TOKEN
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de validez
                .signWith(key) // Con un algoritmo de clave simetrica
                // Esto no tendría sentido si no fuese nuestro... Si es externo, el certificado se firmará con una clave publica / privada
                // El IAM lo firma con su clave privada. Y el IAM me da su clave publica... para que yo pueda validar la firma
                // En este caso, como yo genero y yo valido, con una clave me vale... CLAVE SIMETRICA (Solo 1 clave)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
