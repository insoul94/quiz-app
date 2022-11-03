package data.dao;

import data.eception.IncorrectResponseException;
import data.model.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DaoResponse {

    void save(Response response);
    void update(Response response);
    void updateAllByQuestionId(List<Response> responses, int questionId);
    void delete(Response response);
    List<Response> findResponsesByQuestionId(int questionId);
}
