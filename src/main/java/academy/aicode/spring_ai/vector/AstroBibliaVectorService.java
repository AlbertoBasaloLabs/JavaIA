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

  /**
   * Add documents to the vector store after validating their content and length.
   * 
   * @param documents
   * @return
   */
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

  /**
   * Semantic Search documents in the vector store based on the provided prompt.
   * 
   * @param prompt
   * @return
   */
  public List<Document> searchDocuments(String prompt) {
    return searchDocuments(prompt, 0.4, 2);
  }

  /**
   * Semantic Search documents in the vector store based on the provided prompt
   * with
   * configurable similarity threshold and top K results.
   * 
   * @param prompt              the search query
   * @param similarityThreshold the minimum similarity score (0.0 to 1.0)
   * @param topK                the maximum number of results to return
   * @return
   */
  public List<Document> searchDocuments(String prompt, double similarityThreshold, int topK) {
    if (prompt == null || prompt.trim().isEmpty()) {
      return Collections.emptyList();
    }
    var searchRequest = org.springframework.ai.vectorstore.SearchRequest.builder()
        .query(prompt)
        .similarityThreshold(similarityThreshold)
        .topK(topK)
        .build();
    return vectorRepository.semanticSearchByContent(searchRequest);
  }

}
