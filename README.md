Microservices E-commerce System (Spring Boot + Kafka + GraphQL + MongoDB)
=========================================================================
A full-featured e-commerce microservices architecture built with:
---> Spring Boot (Java)
---> GraphQL APIs
---> Kafka for inter-service communication
---> MongoDB as the primary database
---> Eureka for service discovery
---> Spring Cloud Gateway for routing and JWT-based authentication

Services
========
---> Auth ServiceHandles user authentication and JWT token generation.
---> Product ServiceManages products using a GraphQL API. Stores product data in MongoDB.
---> Order ServiceCreates and tracks orders. Communicates with the Product Service to fetch product info. Publishes order events to Kafka.
---> API GatewayActs as the single entry point with centralized security. Routes traffic to internal services using service discovery.

Features
========
---> Secure with JWT Authentication
---> Built-in Service Discovery using Eureka
---> Event-driven communication via Kafka
---> Non-blocking GraphQL API for the Product Service
---> MongoDB-backed persistence for high scalability
---> Gateway routes based on service name and path
---> Clear separation of responsibilities per microservice

Tech Stack
==========
---> Java, Spring Boot, Spring Cloud, Spring Security
---> JWT AUTH
---> GraphQL (GraphQL Java Tools)
---> MongoDB
---> Apache Kafka
---> Eureka
---> Maven

How to Run
==========
---> Start MongoDB and Kafka locally.
---> Run Eureka Server.
---> Start each service using:
---> mvn spring-boot:run

Use Postman or curl to interact with services.

/** Note **/ will be soon uploading docker files to containerize this app

