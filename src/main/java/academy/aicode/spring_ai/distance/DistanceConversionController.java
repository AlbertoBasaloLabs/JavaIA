package academy.aicode.spring_ai.distance;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing a single endpoint for converting distances between
 * supported units.
 *
 * Validation (fail-fast):
 * - Rejects negative input values.
 * - Rejects unsupported units with a descriptive message listing supported
 * values.
 */
@RestController
@RequestMapping("/api/distance-conversion")
public class DistanceConversionController {
  private static final Logger log = LoggerFactory.getLogger(DistanceConversionController.class);
  private final DistanceConversionService service;

  public DistanceConversionController(DistanceConversionService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<DistanceConversionResponse> convert(@RequestBody DistanceConversionRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Request body must not be empty");
    }
    log.info("Received conversion request: {} {} to {}", request.getInputValue(), request.getInputUnit(),
        request.getOutputUnit());
    if (request.getInputValue() < 0) {
      throw new IllegalArgumentException("Invalid inputValue: must be non-negative");
    }
    var from = ConversionFactorProvider.parseUnit(request.getInputUnit());
    var to = ConversionFactorProvider.parseUnit(request.getOutputUnit());
    double converted = service.convert(request.getInputValue(), from, to);
    double factor = service.getConversionFactor(from, to);
    DistanceConversionResponse resp = new DistanceConversionResponse();
    resp.setOriginalValue(request.getInputValue());
    resp.setOriginalUnit(from.name());
    resp.setConvertedValue(converted);
    resp.setConvertedUnit(to.name());
    resp.setConversionFactor(factor);
    resp.setTimestamp(Instant.now());
    return ResponseEntity.ok(resp);
  }
}
