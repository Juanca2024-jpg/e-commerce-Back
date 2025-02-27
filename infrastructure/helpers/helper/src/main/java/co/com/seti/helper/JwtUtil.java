package co.com.seti.helper;

import co.com.seti.model.common.jwt.JwtUtilGateway;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil implements JwtUtilGateway {

    private final SecretKey key;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(String user, Map<String, Object> claims){

        Instant now = Instant.now();

        return Jwts.builder()
                .claims(claims)
                .subject(user)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationTime, ChronoUnit.HOURS)))
                .signWith( key )
                .compact();
    }

    public Jws<Claims> parseJwt(String jwtString) throws ExpiredJwtException,
            UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
        JwtParser jwtParser = Jwts.parser().verifyWith( key ).build();
        return jwtParser.parseSignedClaims(jwtString);
    }

    public Long getUserId(String token) {
        return ((Integer) parseJwt(token).getPayload().get("id")).longValue();
    }
}
