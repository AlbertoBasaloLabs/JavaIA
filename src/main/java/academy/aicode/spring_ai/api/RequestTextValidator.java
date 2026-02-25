package academy.aicode.spring_ai.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RequestTextValidator {

  public static final int DEFAULT_MAX_LENGTH = 2000;

  public void validateNotBlank(String value, String parameterName) {
    if (value == null || value.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, parameterName + " must not be empty");
    }
  }

  public void validateNotBlankAndMaxLength(String value, String parameterName, int maxLength) {
    validateNotBlank(value, parameterName);
    if (value.length() > maxLength) {
      throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
          parameterName + " is too long; maximum allowed is " + maxLength + " characters");
    }
  }
}
