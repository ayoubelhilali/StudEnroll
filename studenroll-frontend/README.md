# StudEnroll - Student Enrollment Management System

StudEnroll is a modern, enterprise-grade student enrollment application built using a **Spring Cloud microservices architecture** on the backend and a **Vite + React Single Page Application (SPA)** on the frontend.

---

## 🏛️ Project Architecture

A high-fidelity project architecture diagram has been generated and placed in the public directory of the frontend project:

*   **Vector Architecture Diagram:** [project-architecture.svg](./public/project-architecture.svg)
*   **Interactive Architecture Guide:** [project-architecture-guide.html](./public/project-architecture-guide.html) (Open in browser to explore interactive service info and step-by-step request flow)

### Services & Technology Stack

```
                                      +------------------+
                                      |  eureka-server   | (Service Registry)
                                      |   [Port 8761]    |
                                      +--------+---------+
                                               ^
                                               | (Registers/Looks up)
                                               v
+-----------------------+   REST/JSON   +------+-----------+  Route   +---------------------+   JPA/Hibernate   +-----------------+
|  studenroll-frontend  +-------------->+   api-gateway    +--------->+   student-service   +------------------>+    MySQL: DB    |
|   Vite + React SPA    |               |   [Port 8060]    |          |     [Port 8081]     |                   |   student_db    |
|      [Port 5173]      |               +------------------+          +----------+----------+                   +-----------------+
+-----------------------+                                                        ^
                                                                                 | (WebClient)
                                                                      +----------+----------+   JPA/Hibernate   +-----------------+
                                                                      |  enrollment-service  +------------------>+    MySQL: DB    |
                                                                      |     [Port 8083]     |                   |  enrollment_db  |
                                                                      +----------+----------+                   +-----------------+
                                                                                 | (WebClient)
                                                                                 v
                                                                      +----------+----------+   Spring Data     +-----------------+
                                                                      |    course-service   +------------------>+  MongoDB: DB    |
                                                                      |     [Port 8082]     |                   |    course_db    |
                                                                      +---------------------+                   +-----------------+
```

---

## 🔌 Service Catalog & Ports

| Service Name | Port | Primary Technologies | Data Store | Description |
| :--- | :--- | :--- | :--- | :--- |
| **`eureka-server`** | `8761` | Spring Cloud Netflix Eureka | *None* | Central service registry for backend microservices registration & discovery. |
| **`api-gateway`** | `8060` | Spring Cloud Gateway | *None* | Edge routing gateway, implements dynamic path routing and handles CORS mappings. |
| **`student-service`** | `8081` | Spring Boot, Spring Data JPA | MySQL (`student_db`) | Microservice responsible for CRUD operations on student profiles. |
| **`course-service`** | `8082` | Spring Boot, Spring Data MongoDB | MongoDB (`course_db`) | Microservice responsible for course catalog documents. |
| **`enrollment-service`** | `8083` | Spring Boot, WebFlux, JPA | MySQL (`enrollment_db`) | Orchestrates student enrollment in courses. Uses reactive `WebClient` to fetch student and course info. |
| **`studenroll-frontend`** | `5173` | React 19, Vite, Axios | Browser (Client) | User Dashboard Portal for registering students, creating courses, and performing enrollments. |

---

## 🔄 End-to-End System Flow

1.  **User Action:** The client interacts with the React Web SPA at `http://localhost:5173`.
2.  **API Entry:** React fires a REST API call via Axios targeting the API Gateway at `http://localhost:8060/api/...`.
3.  **Discovery Check:** The Gateway checks the `eureka-server` registry to resolve dynamic locations for backend services (`STUDENT-SERVICE`, `COURSE-SERVICE`, or `ENROLLMENT-SERVICE`).
4.  **Routing:** The Gateway forwards the request to the target service instance (e.g. `/api/students/**` -> `STUDENT-SERVICE`).
5.  **Reactive Integration:** During enrollments, the `enrollment-service` contacts `student-service` and `course-service` concurrently via reactive `WebClient` calls to fetch complete models.
6.  **Data Stores:** Individual microservices persist state to their respective database schemas (separated MySQL instances and MongoDB databases).

---

## 🚀 How to Run the Project

### 1. Prerequisite Database Setup
Ensure MySQL and MongoDB are running locally on your computer:
*   **MySQL:** Port `3306` (The services automatically initialize `student_db` and `enrollment_db` schemas via Hibernate `ddl-auto: update`).
*   **MongoDB:** Port `27017` (Creates `course_db` database).

### 2. Startup Order (Backend Services)
In a terminal, run each Spring Boot service in the following order using Maven:
```bash
# 1. Start Eureka Server
cd eureka-server && ./mvnw spring-boot:run

# 2. Start core domain services
cd student-service && ./mvnw spring-boot:run
cd course-service && ./mvnw spring-boot:run
cd enrollment-service && ./mvnw spring-boot:run

# 3. Start API Gateway (Wait until Eureka lists all services)
cd api-gateway && ./mvnw spring-boot:run
```

### 3. Startup Frontend
Navigate to the frontend directory, install dependencies, and start the development server:
```bash
cd studenroll-frontend
npm install
npm run dev
```
Open `http://localhost:5173` in your browser.
