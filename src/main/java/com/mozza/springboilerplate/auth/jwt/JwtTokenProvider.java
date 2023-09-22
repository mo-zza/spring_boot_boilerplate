package com.mozza.springboilerplate.auth.jwt;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.auth.dto.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JwtTokenProvider {
    protected final String secret;

    protected final int accessTokenValidityInHours;

    protected  final int refreshTokenValidityInHours;
    private final Key key;

    public JwtTokenProvider(
            String secret,
            int accessTokenValidityInHours,
            int refreshTokenValidityInHours
    ) {
        this.secret = secret;
        this.accessTokenValidityInHours = accessTokenValidityInHours;
        this.refreshTokenValidityInHours = refreshTokenValidityInHours;

        byte[] keyBytes = Base64.getEncoder().encode(this.secret.getBytes(StandardCharsets.UTF_8));
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponse createToken(UUID memberId, MemberRole role) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, null,
                Collections.singleton(new SimpleGrantedAuthority(role.name())));
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date accessTokenValidity = getValidity(accessTokenValidityInHours);
        Date refreshTokenValidity = getValidity(refreshTokenValidityInHours);

        String accessToken = getToken(authentication, authorities, accessTokenValidity);
        String refreshToken = getToken(authentication, authorities, refreshTokenValidity);

        return TokenResponse.from(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = this.extractClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AuthConstant.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean isMatchedPrefix(String token) {
        return Pattern.matches(AuthConstant.TOKEN_PREFIX_REGEX + " .*", token);
    }

    public String removeTokenPrefix(String token) {
        return token.replaceAll(AuthConstant.TOKEN_PREFIX_REGEX + "( )*", "");
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException error) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token is expired.");
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token is invalid.");
        }
    }

    private String getToken(Authentication authentication, String authorities, Date validity) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AuthConstant.AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
    }

    private Date getValidity(int validityInHours) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, validityInHours);
        return cal.getTime();
    }
}
