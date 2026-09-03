# Spring Boot JWT + STOMP WebSocket Chat

This project is a real-time chat application built with Spring Boot, JWT authentication, and STOMP over WebSocket, integrated with a React frontend.

It demonstrates how to secure WebSocket communication properly using JWT-based authentication at the STOMP interceptor level.

## **Features**
- JWT authentication (user + guest tokens)
- Real-time messaging using WebSocket (SockJS + STOMP)
- Secure STOMP interceptor-based authorization
- Broadcast messaging via /topic/messages
- Role-based access control (USER, GUEST)
- Stateless backend (no HTTP session usage)

## **Purpose**

This project is intended for learning and demonstrating:

- Secure WebSocket architecture in Spring Boot
- STOMP message interception and authentication
- JWT integration beyond REST APIs
- Real-time communication patterns
