# Fitness Club Management System

A streamlined management platform for fitness centers, built using **Spring Boot 3.4** and **Java 17**. This application facilitates member management, class scheduling, and secure administrative control.

---

## 🏗️ Architecture & Security

The application follows a standard layered architecture (Controller, Service, Repository) with **Spring Security** acting as the gatekeeper.

* **Security Implementation:** Integrated via `spring-boot-starter-security` to manage user sessions and protect sensitive management routes.
* **Validation:** Uses **Jakarta Validation** to ensure data integrity for member registrations and workout logs.
* **View Engine:** **Thymeleaf** is utilized for rendering dynamic web pages, providing a seamless server-side UI experience.

---

## 🛠️ Technical Stack

| Component | Technology |
| --- | --- |
| **Backend** | Spring Boot 3.4.0 (Java 17) |
| **Security** | Spring Security 3.4.2 |
| **Database** | MySQL (Production) / Spring Data JPA |
| **UI** | Thymeleaf + HTML5/CSS3 |
| **Monitoring** | Spring Boot Actuator |
| **Utilities** | Project Lombok (Boilerplate reduction) |
| **Optimization** | Spring Cache Abstraction |
| **Automation** | Spring Task Scheduling |

---

## 🔐 Key Features

* **Member Management:** Secure CRUD operations for gym members and staff.
* **Role-Based Access:** Separation of concerns between gym members (viewing schedules) and administrators (managing memberships).
* **Health Monitoring:** Built-in **Actuator** endpoints to monitor application health and performance metrics.
* **Automated Validation:** Robust validation rules for all entity models to prevent invalid data entry.


---

## 🚀 Getting Started

### Prerequisites

* **JDK 17**
* **Maven 3.x**
* **MySQL Server**

### Setup & Installation

1. **Clone the project:**
```bash
git clone https://github.com/your-username/fitness-club.git

```


2. **Database Configuration:**
Configure your MySQL connection in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fitness_db
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

```


3. **Build and Run:**
```bash
mvn clean install
mvn spring-boot:run

```



---

## 🧪 Development

The project includes `spring-boot-devtools` for rapid development (Hot Reloading).

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults());
        return http.build();
    }
}

```

microservice for fitness club app https://github.com/NKumanovv/fitness-history-service


This updated version of your **Fitness Club** project now leverages **Java 21**, uses **Spring Cloud OpenFeign** for external service communication, and is better prepared for testing with **H2** and specific Surefire configurations.

---

# 🏋️ Fitness Club Management System

A high-performance management platform for modern fitness centers, built with **Spring Boot 3.4** and **Java 21 (LTS)**. This application integrates automated background operations, intelligent data caching, and seamless external service communication.

---

## 🏗️ System Architecture

The application is built on a robust foundation designed for performance and security.

### 🛰️ External Integrations (OpenFeign)

The system is equipped with **Spring Cloud OpenFeign**, allowing it to behave as a microservice-ready application. It can easily consume external REST APIs—such as payment gateways, SMS notification services, or global fitness data providers—using a declarative web client.

### 🔐 Security & Access Control

* **Spring Security 3.4.2:** Provides industry-standard authentication and protection against common exploits (CSRF, XSS).
* **Role-Based Access Control (RBAC):** Tailored views and permissions for Members, Trainers, and Admin staff.
* **Data Integrity:** Strict business rule enforcement via **Jakarta Validation**.

### ⚡ Performance & Automation

* **Spring Caching:** Drastically reduces database latency for high-traffic data like workout schedules.
* **Spring Scheduling:** Handles background tasks like membership renewals and automated email reporting.

---

## 🛠️ Technical Stack

| Component | Technology |
| --- | --- |
| **Backend Framework** | Spring Boot 3.4.0 |
| **Language** | Java 21 |
| **HTTP Client** | Spring Cloud OpenFeign |
| **Persistence** | MySQL (Production) / H2 (Testing) |
| **Security** | Spring Security |
| **Templating** | Thymeleaf |
| **Utilities** | Project Lombok |

---

## 🚀 Key Features

* **Member & Subscription Tracking:** Automated status updates and renewal alerts.
* **Declarative API Clients:** Clean integration with external services via Feign interfaces.
* **Real-time Monitoring:** Integrated **Spring Boot Actuator** for health checks and performance metrics.
* **Robust Testing Suite:** Pre-configured with **H2** and `spring-security-test` for high code coverage.

---

## ⚙️ Configuration & Environment

### Prerequisites

* **JDK 21**
* **Maven 3.9+**
* **MySQL 8.0+**

### Setup

1. **Clone the project:**
```bash
git clone https://github.com/your-username/fitness-club.git

```


2. **Configure Properties:** Update `src/main/resources/application.properties` with your database credentials.
3. **Build:**
```bash
mvn clean install

```


4. **Run:**
```bash
mvn spring-boot:run

```



---

## 🧪 Testing

The project uses a specialized Maven Surefire configuration to support **Java 21 dynamic agent loading**, ensuring advanced testing tools and mocks work correctly.

To run the full test suite:

```bash
mvn test

```

Would you like me to create an example **OpenFeign Client** interface for an external service (like a weather API or payment gateway) to include in your project?

