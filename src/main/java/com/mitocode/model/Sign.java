package com.mitocode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Sign {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSign;
    @Column(nullable = false)
    private String temperature;
    @Column(nullable = false)
    private String pulse;
    @Column(nullable = false)
    private String rate;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne //FK
    @JoinColumn(name = "id_patient", nullable = false, foreignKey = @ForeignKey(name = "FK_SIGN_PATIENT"))
    private Patient patient;
}
