package com.mitocode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PatientDTO {

    @EqualsAndHashCode.Include
    private Integer idPatient;

    @Schema(name = "first_name", description = "nombre del cliente")
    @NotNull
    @NotEmpty
    @Size(min = 3, max = 70, message = "{firstname.size}")
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 70, message = "{lastname.size}")
    private String lastName;
    private String dni;
    private String address;

    @Pattern(regexp = "[0-9]+")
    private String phone;

    @Email
    private String email;

    /*
    @Max
    @Min
    @Email
    @Pattern(regexp = "*[a-A-0-9]")
    @Past
    //jakarta validation constraint list
     */

}
