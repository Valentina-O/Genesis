package org.valeneisa.Excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.valeneisa.Dtos.Autenticacion.ErrorRespuesta;

/**
 * Clase encargada de manejar de forma global las excepciones de la aplicación.
 * Permite capturar errores comunes y devolver respuestas estructuradas al cliente.
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    /**
     * Maneja errores de validación generados por anotaciones como
     * @NotBlank, @Email, @Size, entre otras.
     *
     * @param ex Excepción de validación capturada.
     * @return ResponseEntity con el error estructurado y estado HTTP 400.
     */
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

    /**
     * Maneja errores de tipo RuntimeException, generalmente relacionados
     * con la lógica de negocio (por ejemplo: tokens insuficientes o usuario no encontrado).
     *
     * @param ex Excepción capturada.
     * @return ResponseEntity con el error estructurado y estado HTTP 400.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorRespuesta> manejarErroresGenerales(RuntimeException ex) {

        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return ResponseEntity.badRequest().body(error);
    }
}