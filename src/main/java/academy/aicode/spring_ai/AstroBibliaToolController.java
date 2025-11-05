package academy.aicode.spring_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AstroBibliaToolController {

  private final ChatClient chatClient;
  private final WikiToolService wikiToolService;
  private final DistanceConversionToolService distanceConversionToolService;

  public AstroBibliaToolController(ChatClient.Builder chatClientBuilder, WikiToolService wikiToolService,
      DistanceConversionToolService distanceConversionToolService) {
    this.chatClient = chatClientBuilder.build();
    this.wikiToolService = wikiToolService;
    this.distanceConversionToolService = distanceConversionToolService;
  }

  /**
   * Implement wiki mode using calls to Wikipedia or other knowledge base
   * 
   * @param prompt will checked at Wikipedia or other knowledge base
   * 
   * @return
   */
  @GetMapping("tool/ama/wiki")
  public String getFromWiki(String prompt) {
    if (prompt == null || prompt.isBlank()) {
      return "No hay tema un tema que tratar.";
    }
    return chatClient.prompt()
        .system(
            "Responde únicamente con hechos verificables utilizando la información proporcionada. "
                + "Si faltan datos responde 'No pude encontrar información confiable sobre ese tema.'")
        .user(prompt)
        .tools(wikiToolService)
        .call()
        .content();
  }

  @GetMapping("tool/ama/distance")
  public String getDistanceConversion(String prompt) {
    if (prompt == null || prompt.isBlank()) {
      return "No prompt provided.";
    }
    var systemMessage = """
        Eres un asistente útil que convierte distancias astronómicas.
        Usa la herramienta DistanceConverter cuando el usuario pida convertir distancias.
        Las unidades admitidas son: KILÓMETRO, AU, AÑO_LUZ, PARSEC.""";
    return chatClient.prompt()
        .system(systemMessage)
        .user(prompt)
        .tools(distanceConversionToolService)
        .call()
        .content();
  }

  /**
   * Astronomical distance in light time (years, minutes, seconds)
   */
  @GetMapping("tool/ama/distance/light-time")
  public String getDistanceConversionLightTime(String prompt) {
    if (prompt == null || prompt.isBlank()) {
      return "No prompt provided.";
    }
    var systemMessage = """
        Eres un asistente que proporciona distancias de la tierra a otros cuerpos celestes.
        Usa la herramienta DistanceConverter para devolver la distancia en años luz, minutos luz o segundos luz según sea apropiado.""";
    return chatClient.prompt()
        .system(systemMessage)
        .user(prompt)
        .tools(distanceConversionToolService)
        .call()
        .content();
  }
}

// ToDo: Vector DB (ingest, query)