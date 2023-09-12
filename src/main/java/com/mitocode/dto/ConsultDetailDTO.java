package com.mitocode.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ConsultDetailDTO {

    @EqualsAndHashCode.Include
    private Integer idDetail;

    //@JsonIgnore
    @JsonBackReference
    private ConsultDTO consult;

    @NotNull
    private String diagnosis;

    @NotNull
    private String treatment;
}
