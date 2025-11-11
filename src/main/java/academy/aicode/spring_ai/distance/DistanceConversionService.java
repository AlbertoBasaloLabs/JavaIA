package academy.aicode.spring_ai.distance;

import org.springframework.stereotype.Service;

@Service
public class DistanceConversionService {
  public double convert(double value, ConversionFactorProvider.Unit from, ConversionFactorProvider.Unit to) {
    double valueInKm = value * ConversionFactorProvider.getToKilometerFactor(from);
    double factor = ConversionFactorProvider.getToKilometerFactor(to);
    return valueInKm / factor;
  }

  public double getConversionFactor(ConversionFactorProvider.Unit from, ConversionFactorProvider.Unit to) {
    double fromFactor = ConversionFactorProvider.getToKilometerFactor(from);
    double toFactor = ConversionFactorProvider.getToKilometerFactor(to);
    return fromFactor / toFactor;
  }
}
