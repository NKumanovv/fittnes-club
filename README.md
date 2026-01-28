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




