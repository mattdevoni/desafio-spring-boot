package com.gestiontareas.prueba.exception;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testErrorResponseCreation() {
        // Arrange
        int status = 400;
        String error = "Bad Request";
        String message = "Datos inválidos";
        String path = "/api/tareas";

        // Act
        ErrorResponse errorResponse = new ErrorResponse(status, error, message, path);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
        assertTrue(errorResponse.getValidationErrors().isEmpty());
    }

    @Test
    void testAddValidationError() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse(400, "Bad Request", "Datos inválidos", "/api/tareas");
        String field = "nombre";
        String errorMessage = "El nombre no puede estar vacío";

        // Act
        errorResponse.addValidationError(field, errorMessage);

        // Assert
        List<ErrorResponse.ValidationError> errors = errorResponse.getValidationErrors();
        assertEquals(1, errors.size());
        assertEquals(field, errors.get(0).getField());
        assertEquals(errorMessage, errors.get(0).getMessage());
    }

    @Test
    void testMultipleValidationErrors() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse(400, "Bad Request", "Datos inválidos", "/api/tareas");
        
        // Act
        errorResponse.addValidationError("nombre", "El nombre no puede estar vacío");
        errorResponse.addValidationError("descripcion", "La descripción es obligatoria");
        errorResponse.addValidationError("estado", "El estado debe ser válido");

        // Assert
        List<ErrorResponse.ValidationError> errors = errorResponse.getValidationErrors();
        assertEquals(3, errors.size());
    }

    @Test
    void testValidationErrorCreation() {
        // Arrange
        String field = "nombre";
        String message = "El nombre no puede estar vacío";

        // Act
        ErrorResponse.ValidationError validationError = new ErrorResponse.ValidationError(field, message);

        // Assert
        assertEquals(field, validationError.getField());
        assertEquals(message, validationError.getMessage());
        assertNotNull(validationError.toString());
    }

    @Test
    void testErrorResponseToString() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse(400, "Bad Request", "Datos inválidos", "/api/tareas");
        
        // Act & Assert
        assertNotNull(errorResponse.toString());
        assertTrue(errorResponse.toString().contains("400"));
        assertTrue(errorResponse.toString().contains("Bad Request"));
    }
}
