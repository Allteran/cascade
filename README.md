CASCADE
===========
Cascade is retali managment system for service center for the repair of digital equipment. Project built based on microservices architecture.

### Current features:
- CRUD operations with Repair Order and other entities that uses to be part of Repair Order
- Generate XLS documents for organization and customer
- Store and manage Users, Point of Sales, User Roles and all other info that can be related to Workshop

### Tech stack
Whole idea was to re-design existing monolith app [sellper-rest](https://github.com/Allteran/sellper-rest) into ***microservice architecture***
For now it built with:
- 2 different microservices:
  - ***Manager-service*** uses to store and manage main organization data, e.g. Users, Roles, Point of Sales and so on
  - ***Workshop-service*** uses to manipulate with all that related to Repair Order: orders, documents of orders, repairing device types
- Netflix Eureka Discovery Service
- Spring Cloug Project Gateway project that uses to create <api-gateway> to route between services
- MongoDB and PostgreSQL databases
- WebClient from Spring WebFlux Project
- JWT Authentication system
- Vue.js on frontend
- [___in development___] Spring Configuration Server 
- [___in development___] Docker to built it all in one image 
- [___in development___] RabbitMQ 
