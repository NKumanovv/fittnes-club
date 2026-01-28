# 🏋️ Fitness Club Management System

A streamlined management platform for fitness centers, built using **Spring Boot 3.4** and **Java 21**.

---

## 🏗️ System Architecture

The application is built on a robust foundation designed for performance and security.

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
| **Backend** | Spring Boot 3.4.0 (Java 21) |
| **Security** | Spring Security |
| **Persistence** | MySQL (Production) / H2 (Testing) |
| **UI** | Thymeleaf + HTML5/CSS3 |
| **Monitoring** | Spring Boot Actuator |
| **Utilities** | Project Lombok (Boilerplate reduction) |
| **Optimization** | Spring Cache Abstraction |
| **Automation** | Spring Task Scheduling |
| **Templating** | Thymeleaf |
| **HTTP Client** | Spring Cloud OpenFeign |


---

## 🚀 Key Features

* **Declarative API Clients:** Clean integration with external services via Feign interfaces.
* **Real-time Monitoring:** Integrated **Spring Boot Actuator** for health checks and performance metrics.
* **Robust Testing Suite:** Pre-configured with **H2** and `spring-security-test` for high code coverage.


---

## 🚀 Getting Started

### Prerequisites

* **JDK 21**
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

---

## 🧪 Testing

The project uses a specialized Maven Surefire configuration to support **Java 21 dynamic agent loading**, ensuring advanced testing tools and mocks work correctly.

To run the full test suite:

```bash
mvn test

```

Microservice for fitness club app https://github.com/NKumanovv/fitness-history-service


