<img width="1918" height="700" alt="image" src="https://github.com/user-attachments/assets/ab33bef5-8204-4182-9713-b9d30c18d0df" />

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

2. **Create MySQL database**
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
or run Main.java from your IDE
```bash
   mvn spring-boot:run
```

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
src/main/java/com/enrollapp/
├── controller/     # Web controllers (HomeController)
├── model/          # Entity classes
├── repository/     # JPA repositories
├── service/        # Business logic
└── View/           # JSF backing beans
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

<img width="1080" height="1920" alt="photo-collage png (2)" src="https://github.com/user-attachments/assets/a2799b7a-ec01-45c2-b462-2123e05b5a9a" />

<img width="1080" height="1920" alt="photo-collage png (1)" src="https://github.com/user-attachments/assets/13458dc7-ece6-47a7-9976-0ff912c6105e" />


