package com.gestiontareas.prueba.service.impl;

import com.gestiontareas.prueba.dto.auth.JwtResponse;
import com.gestiontareas.prueba.dto.auth.LoginRequest;
import com.gestiontareas.prueba.dto.auth.SignupRequest;
import com.gestiontareas.prueba.model.Usuario;
import com.gestiontareas.prueba.repository.UsuarioRepository;
import com.gestiontareas.prueba.security.jwt.JwtUtils;
import com.gestiontareas.prueba.security.services.UserDetailsImpl;
import com.gestiontareas.prueba.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail());
    }

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signupRequest) {
        if (usuarioRepository.existsByUsername(signupRequest.getUsername())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: El nombre de usuario ya está en uso");
            return ResponseEntity.badRequest().body(response);
        }

        if (usuarioRepository.existsByEmail(signupRequest.getEmail())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: El email ya está en uso");
            return ResponseEntity.badRequest().body(response);
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario(
            signupRequest.getUsername(),
            encoder.encode(signupRequest.getPassword()),
            signupRequest.getNombre(),
            signupRequest.getApellido(),
            signupRequest.getEmail()
        );

        usuarioRepository.save(usuario);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario registrado exitosamente");
        return ResponseEntity.ok(response);
    }
}
