package old12t_it.cinematrix.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import old12t_it.cinematrix.security.service.UserDetailImp;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtTokenService {
    private final String JWT_SECRET = "tHszemdNwLncrex4an1Dy5EKnT0p16vDfhiRmhpbW4x88PXb3RWswclikrRg3VqDMzQEGeiMGa7gxEGkrhVYHA";
    private final long JWT_EXPIRATION = 21600000;

    public String extractUserEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Authentication auth) {
        var payload = (UserDetailImp) auth.getPrincipal();
        return Jwts.builder()
                .claims(new HashMap<>()).subject(payload.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSignInKey(), Jwts.SIG.HS512)
                .compact();
    }

    public Boolean validateToken(String jwtToken) {
        try {
            extractAllClaims(jwtToken);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            return false;
        }
    }

    private Boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] secretKey = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(secretKey);
    }
}
