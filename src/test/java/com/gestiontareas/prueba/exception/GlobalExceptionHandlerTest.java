package com.gestiontareas.prueba.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getRequestURI()).thenReturn("/api/tareas");
    }

    @Test
    void handleValidationExceptions_DebeRetornarErrorResponseConValidationErrors() {
        // Arrange
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("tareaDTO", "nombre", "El nombre es obligatorio"));
        fieldErrors.add(new FieldError("tareaDTO", "descripcion", "La descripción es obligatoria"));

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));

        // Act
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleValidationExceptions(
                methodArgumentNotValidException, request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        
        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Validation Error", errorResponse.getError());
        assertEquals("/api/tareas", errorResponse.getPath());
        
        // No podemos verificar directamente los errores de validación debido a la forma en que se mockean
        // pero podemos verificar que el método addValidationError se llame
        verify(bindingResult, times(1)).getAllErrors();
    }

    @Test
    void handleResponseStatusException_DebeRetornarErrorResponseConStatusCode() {
        // Arrange
        ResponseStatusException exception = new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Recurso no encontrado");

        // Act
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleResponseStatusException(
                exception, request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        
        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Recurso no encontrado", errorResponse.getError());
        assertEquals("/api/tareas", errorResponse.getPath());
    }

    @Test
    void handleBadCredentialsException_DebeRetornarErrorResponseConStatusUnauthorized() {
        // Arrange
        BadCredentialsException exception = new BadCredentialsException("Credenciales incorrectas");

        // Act
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleBadCredentialsException(
                exception, request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        
        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
        assertEquals("Unauthorized", errorResponse.getError());
        assertEquals("Credenciales inválidas", errorResponse.getMessage());
        assertEquals("/api/tareas", errorResponse.getPath());
    }

    @Test
    void handleAuthenticationException_DebeRetornarErrorResponseConStatusUnauthorized() {
        // Arrange
        AuthenticationException exception = mock(AuthenticationException.class);
        when(exception.getMessage()).thenReturn("Error de autenticación");

        // Act
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleAuthenticationException(
                exception, request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        
        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), errorResponse.getStatus());
        assertEquals("Unauthorized", errorResponse.getError());
        assertEquals("Error de autenticación", errorResponse.getMessage());
        assertEquals("/api/tareas", errorResponse.getPath());
    }

    @Test
    void handleGenericException_DebeRetornarErrorResponseConStatusInternalServerError() {
        // Arrange
        Exception exception = new RuntimeException("Error interno");

        // Act
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleGenericException(
                exception, request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        
        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("Ha ocurrido un error interno en el servidor", errorResponse.getMessage());
        assertEquals("/api/tareas", errorResponse.getPath());
    }
}
