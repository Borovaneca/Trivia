package bg.trivia.repostiories;

import bg.trivia.model.entities.CsharpQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsharpQuestionRepository extends MongoRepository<CsharpQuestion, String> {
}
