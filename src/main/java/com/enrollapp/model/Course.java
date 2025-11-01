package com.enrollapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "Courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "credit_hours")
    private Integer creditHours;

    @Column(name = "is_active")
    private Boolean isActive;

    @Lob
    private String description;

    // Using CascadeType.PERSIST to automatically save the referenced entity (FK) if not saved
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Faculty faculty;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "current_enrollment")
    private Integer currentEnrollment=0;

    @ManyToMany
    @JoinTable(
            name = "course_prerequisites",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private Set<Course> prerequisites;
}
