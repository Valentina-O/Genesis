package org.valeneisa.Excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.valeneisa.Dtos.Autenticacion.ErrorRespuesta;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    // Errores de @NotBlank, @Email, @Size
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        // Obtenemos el primer mensaje de error para no saturar la respuesta
        String mensaje = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.BAD_REQUEST.value(),
                mensaje
        );

        return ResponseEntity.badRequest().body(error);
    }

    // Errores de lógica (como "Tokens insuficientes" o "Usuario no encontrado")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorRespuesta> manejarErroresGenerales(RuntimeException ex) {

        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return ResponseEntity.badRequest().body(error);
    }
}