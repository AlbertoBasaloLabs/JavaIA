package academy.aicode.spring_ai.astro;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class AstroBibliaBasicController {
  private final ChatClient chatClient;

  private static final String ASTRONOMY_SYSTEM_MESSAGE = "Eres un experto en Astronomía. Responde solo preguntas relacionadas con la Astronomía. Si la pregunta no está relacionada con la Astronomía, responde con 'No sé sobre ese tema.'";

  public AstroBibliaBasicController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  /**
   * Aka "Ask Me Anything"
   * 
   * @param prompt
   * @return
   */
  @GetMapping("basic/ama")
  public String getAnything(@RequestParam String prompt) {

    return chatClient.prompt()
        .user(prompt).call().content();
  }

  /**
   * Ask only Astronomy related questions. The other topics are treated as not
   * known.
   * 
   * @param prompt
   * @return
   */
  @GetMapping("basic/astro")
  public String getAstronomy(@RequestParam String prompt) {
    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(prompt).call().content();
  }

  /**
   * Ask about a planet. The planet is provided as a parameter.
   * Use a string replacement for crafting the user prompt
   * 
   * @param planet
   * @return
   */
  @GetMapping("basic/planet")
  public String getPlanetInfo(@RequestParam String planet) {
    var userPromptTemplate = "Proporciona una breve descripción del planeta {{planet}} incluyendo sus características clave y cualquier dato interesante.";
    var userPrompt = userPromptTemplate.replace("{{planet}}", planet);

    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(userPrompt).call().content();
  }

  /**
   * Ask about a planet's satellites. The planet is provided as a parameter.
   * Use lambda to craft the user prompt
   * 
   * @param planet
   * @return
   */
  @GetMapping("basic/planet/satellites")
  public String getPlanetSatellites(@RequestParam String planet) {

    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(u -> {
          u.text("¿Cuáles son los satélites del planeta {planet} ?");
          u.param("planet", planet);
        }).call().content();
  }

  /**
   * Ask about a planet's satellites. The planet is provided as a parameter.
   * Return a structured response using records
   * 
   * @param planet
   * @return
   */
  @GetMapping("basic/planet/satellites/structured")
  public Satellites getPlanetSatellitesStructured(@RequestParam String planet) {

    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(u -> {
          u.text("¿Cuáles son los satélites del planeta {planet} ?");
          u.param("planet", planet);
        }).call().entity(Satellites.class);
  }

  /**
   * Ask about a planet's satellites. The planet is provided as a parameter.
   * Respond in a streaming fashion
   * 
   * @param planet
   * @return
   */
  @GetMapping("basic/planet/satellites/stream")
  public Flux<String> getPlanetSatellitesStream(@RequestParam String planet) {

    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(u -> {
          u.text("¿Cuáles son los satélites del planeta {planet} ?");
          u.param("planet", planet);
        }).stream().content();
  }

}

record Satellite(String name, double radius, double mass) {
}

record Satellites(List<Satellite> satellites) {
}