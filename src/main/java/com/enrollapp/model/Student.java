package com.enrollapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Using @Table(uniqueConstraints) instead of @Column(unique = true)
// to explicitly name the constraints for better readability and database debugging.

@Entity
@Table(
        name = "Students",
        uniqueConstraints = {
                @UniqueConstraint(name = "national_id_unique", columnNames = {"national_id"}),
                @UniqueConstraint(name = "email_unique", columnNames = {"email"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "national_id")
    @NotNull(message = "National ID is required")
    private Integer nationalId;

    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "mobile_no")
    private String mobileNo;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "school_avg")
    private Double schoolAvg;

    // Using CascadeType.PERSIST to automatically save the referenced entity (FK) if not saved
    // avoiding the need for a separate repository.
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Nationality nationality;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Faculty faculty;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Major major;
}