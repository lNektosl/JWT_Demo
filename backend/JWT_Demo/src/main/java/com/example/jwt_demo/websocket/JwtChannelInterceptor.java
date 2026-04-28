package com.example.jwt_demo.websocket;

import com.example.jwt_demo.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    public JwtChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    Logger log = LoggerFactory.getLogger(JwtChannelInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }
        log.info("STOMP CONNECT headers: " + accessor.toNativeHeaderMap());
        StompCommand command = accessor.getCommand();

        // 🔐 CONNECT — аутентификация
        if (StompCommand.CONNECT.equals(command)) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing Authorization header");
            }

            String token = jwtUtil.extractToken(authHeader);

            if (!jwtUtil.validateToken(token)) {
                throw new IllegalArgumentException("Invalid JWT");
            }

            String username = jwtUtil.getUsernameFromToken(token);
            List<String> roles = jwtUtil.getRolesFromToken(token);

            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> (GrantedAuthority)new  SimpleGrantedAuthority(role))
                    .toList();

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            accessor.setUser(auth);
        }

        // 🔐 SEND / SUBSCRIBE — авторизация
        if (StompCommand.SEND.equals(command) ||
                StompCommand.SUBSCRIBE.equals(command)) {

            if (accessor.getUser() == null) {
                throw new IllegalArgumentException("Unauthorized STOMP message");
            }
        }

        return message;
    }
}
