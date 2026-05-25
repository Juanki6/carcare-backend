package com.example.carcare.controller;

import com.example.carcare.dto.LoginResponseDTO;
import com.example.carcare.dto.UsuarioDto;
import com.example.carcare.dto.UsuarioLoginRequestDTO;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.Response;
import com.example.carcare.security.JwtService;
import com.example.carcare.service.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Usuarios", description = "Gestión de usuarios: registro, login y CRUD")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // ─── ENDPOINTS PÚBLICOS ───────────────────────────────────────────────────

    @Operation(summary = "Registrar nuevo usuario", description = "Crea la cuenta con rol ROLE_USER y contraseña hasheada con BCrypt. Campo 'passwordHash' debe contener la contraseña en texto plano.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto nuevo = usuarioService.registro(usuarioDto);
            nuevo.setPasswordHash(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (CreateEntityException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Response.generalError(HttpStatus.CONFLICT.value(), ex.getMessage()));
        }
    }

    @Operation(summary = "Login de usuario", description = "Valida credenciales y devuelve un token JWT. Enviar email y password en el body.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login correcto, se devuelve el token JWT"),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.generalError(HttpStatus.UNAUTHORIZED.value(), "Credenciales incorrectas"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        String token = jwtService.generateToken(userDetails);

        UsuarioDto usuarioDto = usuarioService.findByEmail(loginRequest.getEmail()).orElseThrow();

        // 👇 AÑADE ESTOS LOGS 👇
        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("Email: " + usuarioDto.getEmail());
        System.out.println("ID: " + usuarioDto.getIdUsuario());
        System.out.println("Rol: " + usuarioDto.getRol());
        System.out.println("==================");

        usuarioDto.setPasswordHash(null);

        LoginResponseDTO response = new LoginResponseDTO(token, usuarioDto);
        System.out.println("Response ID: " + response.getUsuario().getIdUsuario()); // 👈 Otro log

        return ResponseEntity.ok(response);
    }

    // ─── ENDPOINTS PROTEGIDOS ─────────────────────────────────────────────────

    @Operation(summary = "Obtener todos los usuarios (solo ADMIN)")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
        List<UsuarioDto> usuarios = usuarioService.findAll();
        usuarios.forEach(u -> u.setPasswordHash(null));
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Obtener usuario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "500", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getUsuarioById(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        UsuarioDto usuario = usuarioService.findById(id);
        usuario.setPasswordHash(null);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Buscar usuario por email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/email")
    public ResponseEntity<UsuarioDto> getUsuarioByEmail(
            @Parameter(description = "Email del usuario") @RequestParam String email) {
        Optional<UsuarioDto> usuario = usuarioService.findByEmail(email);
        usuario.ifPresent(u -> u.setPasswordHash(null));
        return usuario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar usuario existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @Valid @RequestBody UsuarioDto usuarioDto) {
        UsuarioDto actualizado = usuarioService.update(id, usuarioDto);
        actualizado.setPasswordHash(null);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar usuario por ID (solo ADMIN)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "500", description = "Usuario no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Comprobar si existe un email")
    @ApiResponse(responseCode = "200", description = "true si el email ya está registrado")
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(
            @Parameter(description = "Email a comprobar") @RequestParam String email) {
        return ResponseEntity.ok(usuarioService.existsByEmail(email));
    }
}
