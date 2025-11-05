package academy.aicode.spring_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AstroBibliaToolController {

  private final ChatClient chatClient;
  private final WikiToolService wikiToolService;

  public AstroBibliaToolController(ChatClient.Builder chatClientBuilder, WikiToolService wikiToolService) {
    this.chatClient = chatClientBuilder.build();
    this.wikiToolService = wikiToolService;
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
}

// ToDo: Tools (fetch, UA converter)
// ToDo: Vector DB (ingest, query)