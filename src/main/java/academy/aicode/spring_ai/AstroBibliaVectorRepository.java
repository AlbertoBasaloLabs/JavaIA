package academy.aicode.spring_ai;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Repository;

@Repository
public class AstroBibliaVectorRepository {
  private final VectorStore vectorStore;

  public AstroBibliaVectorRepository(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  public void addDocuments(List<Document> docs) {
    vectorStore.add(docs);
  }

  public List<Document> semanticSearchByContent(SearchRequest searchRequest) {
    return vectorStore.similaritySearch(searchRequest);
  }
}