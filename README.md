# EnrollApp
A Spring Boot + JSF web application for managing student enrollments, courses, and grades with prerequisite validation and GPA calculation.

## Features
- **Student Management** - CRUD operations for students with faculty/major assignments
- **Course Management** - Create courses with prerequisites and capacity limits
- **Enrollment System** - Enroll students with automatic prerequisite checking
- **Grade Management** - Assign grades and calculate weighted GPA by credit hours
- **Semester Management** - Active/inactive semester control

## Tech Stack
- **Backend:** Spring Boot 3.x, JPA/Hibernate
- **Frontend:** JSF 4.0, PrimeFaces
- **Database:** MySQL 8.0
- **Build Tool:** Maven

## Requirements
- Java 17+
- Maven 3.6+
- MySQL 8.0+

## Setup

1. **Clone and navigate to project**
```bash
   git clone <your-repo>
   cd EnrollApp
```

2. **Create database**
```sql
   CREATE DATABASE EnrollApp;
```

3. **Configure database** - Edit `src/main/resources/application.properties`:
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/EnrollApp
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
```

4. **Run application**
```bash
   mvn spring-boot:run
```
or run Main.java from your IDE.

5. **Access application**
    - Main: http://localhost:9090/
    - Enrollment: http://localhost:9090/enrollment.xhtml
    - Grades: http://localhost:9090/grades.xhtml

## Usage
1. Create faculties, majors, nationalities
2. Add students and courses
3. Set semester as active
4. Enroll students (prerequisites auto-checked)
5. Assign grades and calculate GPA

## Project Structure
```
src/main/java/com/Batjsf/
├── controller/     # Web controllers (HomeController)
├── model/          # Entity classes
├── repository/     # JPA repositories
├── service/        # Business logic
└── View/           # JSF backing beans
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
