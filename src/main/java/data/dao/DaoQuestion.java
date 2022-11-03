package data.dao;

import data.eception.IncorrectQuestionException;
import data.eception.IncorrectResponseException;
import data.model.Question;

import java.sql.SQLException;
import java.util.List;

public interface DaoQuestion {

    void save(Question question) throws IncorrectQuestionException, IncorrectResponseException;
    void update(Question question) throws IncorrectQuestionException, IncorrectResponseException;
    void delete(Question question);
    List<Question> findByTopic(String topic) throws IncorrectQuestionException, IncorrectResponseException;
}
