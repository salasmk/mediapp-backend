package com.mitocode.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SignDTO {

    @EqualsAndHashCode.Include
    private Integer idSign;

    @NotNull
    @NotEmpty
    private String temperature;

    @NotNull
    @NotEmpty
    private String pulse;

    @NotNull
    @NotEmpty
    private String rate;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private PatientDTO patient;

}
