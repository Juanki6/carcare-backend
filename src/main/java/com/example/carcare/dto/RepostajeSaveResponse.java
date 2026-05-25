package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepostajeSaveResponse {
    private boolean duplicado;
    private String advertencia;
    private RepostajeDto repostaje;
}
