package com.panda.pandasocialmediaback.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(authentication1 -> {
                    try {
                        return jwtProvider.getClaims(authentication.getCredentials().toString());
                    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                        throw new RuntimeException(e);
                    }
                })
                .log()
                .onErrorResume(e->Mono.error(new RuntimeException(e)))
                .map(claims -> new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        Stream.of(claims.get("roles"))
                                .map(role->(List<Map<String, String>>)role)
                                .flatMap(role->role.stream()
                                        .map(r->r.get("authority"))
                                        .map(SimpleGrantedAuthority::new))
                                .collect(Collectors.toList())
                ));
    }
}
