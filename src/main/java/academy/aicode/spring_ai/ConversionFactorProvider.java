package academy.aicode.spring_ai;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConversionFactorProvider {
  public enum Unit {
    KILOMETER, AU, LIGHT_YEAR, PARSEC
  }

  private static final Map<Unit, Double> TO_KILOMETER;
  static {
    Map<Unit, Double> map = new HashMap<>();
    map.put(Unit.KILOMETER, 1.0);
    map.put(Unit.AU, 149_597_870.7); // IAU 2012: 1 AU = 149,597,870.7 km
    map.put(Unit.LIGHT_YEAR, 9_460_730_472_580.8); // 1 ly = 9,460,730,472,580.8 km
    map.put(Unit.PARSEC, 30_856_775_814_913.672); // 1 pc = 30,856,775,814,913.672 km
    TO_KILOMETER = Collections.unmodifiableMap(map);
  }

  public static boolean isSupported(String unit) {
    try {
      Unit.valueOf(unit.toUpperCase());
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static double getToKilometerFactor(Unit unit) {
    return TO_KILOMETER.get(unit);
  }

  public static Unit parseUnit(String unit) {
    return Unit.valueOf(unit.toUpperCase());
  }

  public static Map<Unit, Double> getAllFactors() {
    return TO_KILOMETER;
  }
}
