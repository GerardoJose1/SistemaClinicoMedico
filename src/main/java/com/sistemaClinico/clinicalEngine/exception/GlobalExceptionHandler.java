package com.sistemaClinico.clinicalEngine.exception;

import com.sistemaClinico.clinicalEngine.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

/**
 * Manejador global de excepciones.
 * Esta clase intercepta errores en toda la aplicación y los formatea como respuestas JSON consistentes.
 */
public class GlobalExceptionHandler {
    /**
     * Captura todas las excepciones genéricas que no han sido manejadas específicamente.
     * Retorna un error 500 (Internal Server Error).
     * * @param ex La excepción capturada.
     * @param request Información del contexto de la petición.
     * @return Un objeto ErrorDto con detalles del fallo.
     */
    @ExceptionHandler(Exception.class) // Captura todas las excepciones no manejadas
    public ResponseEntity<ErrorDto> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDto error = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Interno",
                ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja errores específicos de llamadas HTTP externas (RestTemplate).
     * Útil cuando WordPress o EspoCRM devuelven errores como 401 Unauthorized o 404 Not Found.
     * * @param ex Excepción de tipo HttpClientErrorException.
     * @param request Información de la petición.
     * @return ResponseEntity con el mismo código de estado que devolvió el servicio externo.
     */
    @ExceptionHandler(HttpClientErrorException.class) // Específico para errores de RestTemplate
    public ResponseEntity<ErrorDto> handleHttpClientError(HttpClientErrorException ex, WebRequest
            request) {

        ErrorDto error = new ErrorDto(ex.getStatusCode().value(), ex.getStatusText(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(error, ex.getStatusCode());
    }
}
