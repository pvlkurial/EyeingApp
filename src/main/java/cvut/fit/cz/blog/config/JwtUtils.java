package cvut.fit.cz.blog.config;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "9a4722616018ee81240851db2eecee76ae084f88ea78f2c43069d86cb77b74fc834b6e0c0a6170b2ec7a3c900a5126823f438468d24a21a98f0edea97bd5a18051a2967dc43ed68f0d6372cb7220b144eca2b41ffa23cba9d746e04a4848be7513bba26018ddd8b6eb475c3ddcbbb6fa5b210d5006230f8ff0eb14124a9c1ebcfcdeb1a1fa8676b9f436ae25302836b3738842ab8739f95d7fd6e5709740a598ffadf385a147e316263135556de5e9d7989fd1f1fb3580301c0456cd7e3ed607ead60859133822b5d655eff52731e49855d19ed3eeadef4384163d40c074a689e60b1d617522b4d741751b43499fccff4da1707e2fc7b7bf78b35cd98671b8ab";
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}