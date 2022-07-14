package pr5;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import pr4.Product;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JWT {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String createJWT(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(key).compact();
    }
    public static String extractUserName(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(jwt)
                .getBody()
                .getSubject();
    }
}
