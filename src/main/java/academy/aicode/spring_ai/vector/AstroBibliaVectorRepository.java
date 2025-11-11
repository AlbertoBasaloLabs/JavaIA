package academy.aicode.spring_ai.vector;

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

  /**
   * Add documents to the vector store.
   * 
   * @param docs
   */
  public void addDocuments(List<Document> docs) {
    vectorStore.add(docs);
  }

  /**
   * Semantic Search documents in the vector store based on the provided search
   * request.
   * 
   * @param searchRequest
   * @return
   */
  public List<Document> semanticSearchByContent(SearchRequest searchRequest) {
    return vectorStore.similaritySearch(searchRequest);
  }
}
