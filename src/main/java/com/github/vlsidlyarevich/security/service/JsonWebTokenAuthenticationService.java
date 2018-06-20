package com.github.vlsidlyarevich.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vlsidlyarevich.exception.model.UserNotFoundException;
import com.github.vlsidlyarevich.model.User;
import com.github.vlsidlyarevich.model.UserAuthentication;
import com.github.vlsidlyarevich.security.constants.SecurityConstants;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
public class JsonWebTokenAuthenticationService implements TokenAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenAuthenticationService.class);
    @Value("${security.token.secret.key}")
    private String secretKey;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JsonWebTokenAuthenticationService(final BasicUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(final HttpServletRequest request) {
        final String token = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
        final Jws<Claims> tokenData = parseToken(token);
        if (tokenData != null) {
            User user = getUserFromToken(tokenData);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }

    private Jws<Claims> parseToken(String token) {

        if (token != null) {
            try {
                if(token.startsWith("Bearer ")){
                    token = token.replaceFirst("Bearer ","");
                }
                return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(String.valueOf(token));
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                    | SignatureException | IllegalArgumentException e) {
                logger.debug("Could not parse token '{}'",token,e);
            }
        }
        return null;
    }

    private User getUserFromToken(final Jws<Claims> tokenData) {
        try {
            return (User) userDetailsService
                    .loadUserByUsername(tokenData.getBody().get("username").toString());
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("User "
                    + tokenData.getBody().get("username").toString() + " not found");
        }
    }
}
