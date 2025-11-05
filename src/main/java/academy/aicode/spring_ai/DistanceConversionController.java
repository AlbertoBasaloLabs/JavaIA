package academy.aicode.spring_ai;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import academy.aicode.distance.ConversionFactorProvider;
import academy.aicode.distance.DistanceConversionRequest;
import academy.aicode.distance.DistanceConversionResponse;
import academy.aicode.distance.DistanceConversionService;

@RestController
@RequestMapping("/api/distance-conversion")
public class DistanceConversionController {
  private static final Logger logger = LoggerFactory.getLogger(DistanceConversionController.class);
  private final DistanceConversionService service = new DistanceConversionService();

  @PostMapping
  public ResponseEntity<?> convert(@RequestBody DistanceConversionRequest request) {
    logger.info("Received conversion request: {} {} to {}", request.getInputValue(), request.getInputUnit(),
        request.getOutputUnit());
    if (request.getInputValue() < 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input value must be non-negative");
    }
    if (!ConversionFactorProvider.isSupported(request.getInputUnit())
        || !ConversionFactorProvider.isSupported(request.getOutputUnit())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Unsupported unit. Supported: KILOMETER, AU, LIGHT_YEAR, PARSEC");
    }
    ConversionFactorProvider.Unit from = ConversionFactorProvider.parseUnit(request.getInputUnit());
    ConversionFactorProvider.Unit to = ConversionFactorProvider.parseUnit(request.getOutputUnit());
    double converted = service.convert(request.getInputValue(), from, to);
    double factor = service.getConversionFactor(from, to);
    DistanceConversionResponse resp = new DistanceConversionResponse();
    resp.setOriginalValue(request.getInputValue());
    resp.setOriginalUnit(request.getInputUnit().toUpperCase());
    resp.setConvertedValue(converted);
    resp.setConvertedUnit(request.getOutputUnit().toUpperCase());
    resp.setConversionFactor(factor);
    resp.setTimestamp(Instant.now());
    return ResponseEntity.ok(resp);
  }
}
