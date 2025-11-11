package academy.aicode.spring_ai.astro;

import java.util.List;
import java.util.Map;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import academy.aicode.spring_ai.vector.AstroBibliaVectorService;
import academy.aicode.spring_ai.vector.DocumentRequest;

@RestController
public class AstroBibliaVectorController {
  private final EmbeddingModel embeddingModel;
  private final AstroBibliaVectorService vectorService;

  public AstroBibliaVectorController(EmbeddingModel embeddingModel, AstroBibliaVectorService vectorService) {
    this.embeddingModel = embeddingModel;
    this.vectorService = vectorService;
  }

  @GetMapping("/vector/embedding")
  public Map<String, Object> embed(@RequestParam() String message) {
    EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
    return Map.of("embedding", embeddingResponse);
  }

  @GetMapping("/vector/ingest")
  public Map<String, Object> ingest(@RequestParam() String message) {
    var addDocuments = this.vectorService
        .addDocuments(List.of(
            new DocumentRequest(
                message,
                Map.of("source", "AstroBiblia", "length", message.length()))));
    return Map.of("added", addDocuments.size());
  }
}
