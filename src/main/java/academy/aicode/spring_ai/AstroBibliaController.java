package academy.aicode.spring_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AstroBibliaController {
  private final ChatClient chatClient;

  public AstroBibliaController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  /**
   * Aka "Ask Me Anything"
   * 
   * @param prompt
   * @return
   */
  @GetMapping("/ama")
  public String getAnything(@RequestParam String prompt) {

    return chatClient.prompt()
        .user(prompt).call().content();
  }
}
