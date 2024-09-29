package bg.trivia.services;

import bg.trivia.exceptions.InvalidInputException;
import bg.trivia.model.dtos.QuestionDTO;
import bg.trivia.model.entities.Question;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.ui.ModelMapExtensionsKt;

import java.util.List;

@Service
public class QuestionService {


    private MongoTemplate mongoTemplate;
    private ModelMapper mapper;

    @Autowired
    public QuestionService(MongoTemplate mongoTemplate, ModelMapper mapper) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    public List<QuestionDTO> get10Questions(String technology, String difficulty) {
        validInputs(technology, difficulty);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("difficulty").is(difficulty)),
                Aggregation.sample(10)
        );

        AggregationResults<Question> results = mongoTemplate.aggregate(agg, technology +"-questions", Question.class);
        return results.getMappedResults().stream().map(s -> mapper.map(s, QuestionDTO.class)).toList();
    }

    private void validInputs(String technology, String difficulty) {
        boolean validTechnology = switch (technology) {
            case "java", "csharp", "python", "javascript" -> true;
            default -> false;
        };

        if (!validTechnology) throw new InvalidInputException("Technology type not valid");

        boolean validDifficulty = switch (difficulty) {
            case "Easy", "Medium", "Hard" -> true;
            default -> false;
        };

        if (!validDifficulty) throw new InvalidInputException("Difficulty not valid");
    }
}
