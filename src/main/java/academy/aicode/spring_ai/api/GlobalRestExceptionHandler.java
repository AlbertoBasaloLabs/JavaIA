package academy.aicode.spring_ai.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalRestExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalRestExceptionHandler.class);

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<String> handleResponseStatusException(ResponseStatusException exception) {
    var status = exception.getStatusCode();
    var message = resolveMessage(exception.getReason(), status.is4xxClientError());
    return ResponseEntity.status(status).body(message);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
    return ResponseEntity.badRequest().body(resolveMessage(exception.getMessage(), true));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<String> handleMissingParameter(MissingServletRequestParameterException exception) {
    var message = "Missing required parameter: " + exception.getParameterName();
    return ResponseEntity.badRequest().body(message);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleUnreadableBody(HttpMessageNotReadableException exception) {
    return ResponseEntity.badRequest().body("Invalid request body: check JSON format and required fields");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleUnexpectedException(Exception exception) {
    log.error("Unhandled REST exception", exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Unexpected server error while processing the request");
  }

  private String resolveMessage(String message, boolean clientError) {
    if (message != null && !message.isBlank()) {
      return message;
    }
    if (clientError) {
      return "Request could not be processed due to invalid input";
    }
    return "Unexpected server error while processing the request";
  }
}
