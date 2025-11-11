package academy.aicode.spring_ai.vector;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class AstroBibliaVectorService {

  private static final int MAX_TOKENS = (int) (8192 * 0.80);
  private final AstroBibliaVectorRepository vectorRepository;

  public AstroBibliaVectorService(AstroBibliaVectorRepository vectorRepository) {
    this.vectorRepository = vectorRepository;
  }

  public List<Document> addDocuments(List<DocumentRequest> documents) {
    if (documents == null || documents.isEmpty()) {
      return Collections.emptyList();
    }
    var docs = documents.stream()
        .filter(doc -> doc != null && doc.getContent() != null && !doc.getContent()
            .trim()
            .isEmpty())
        .filter(doc -> {
          int wordCount = doc.getContent()
              .split("\\s+").length;
          return wordCount <= MAX_TOKENS;
        })
        .map(doc -> new Document(doc.getContent(), doc.getMetadata()))
        .collect(Collectors.toList());
    vectorRepository.addDocuments(docs);
    return docs;
  }

}
