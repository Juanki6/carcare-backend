package com.example.carcare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto implements Serializable {
    private Long idUsuario;

    @NotNull(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    private String passwordHash;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String rol;
}