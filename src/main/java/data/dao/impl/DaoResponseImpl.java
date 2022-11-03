package data.dao.impl;

import data.util.DBUtils;
import data.dao.DaoResponse;
import data.model.Response;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoResponseImpl implements DaoResponse {

    private static final Logger LOGGER = Logger.getLogger(DaoResponseImpl.class.getName());

    @Override
    public void save(Response response) {
        try(Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO quiz.response (fk_question_id, text, correct) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, response.getQuestionId());
            statement.setString(2, response.getText());
            statement.setInt(3, response.isCorrect() ? 1 : 0);

            int count = statement.executeUpdate();
            if (count == 0) {
                throw new SQLException("Cannot save response");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    int responseId = keys.getInt(1);
                    response.setResponseId(responseId);
                }
            }
        } catch (SQLException e) {
            log("SAVE failed", e);
        }
    }


    @Override
    public void update(Response response) {
        try(Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE quiz.response SET fk_question_id = ?, text = ?, correct = ? WHERE response_id = ?;")) {

            statement.setInt(1, response.getQuestionId());
            statement.setString(2, response.getText());
            statement.setInt(3, response.isCorrect() ? 1 : 0);
            statement.setInt(4, response.getResponseId());

            int result = statement.executeUpdate();
            if (result == 0) {
                throw new SQLException(String.format(
                        "No response with ID:%d was found", response.getResponseId()));
            }
        } catch (SQLException e) {
            log("UPDATE failed", e);
        }
    }


    public void updateAllByQuestionId(List<Response> responses, int questionId) {

        List<Response> newResponses = new ArrayList<>(responses);

        // Check whether all responses are related to the question.
        newResponses.removeIf(response -> response.getQuestionId() != questionId);

        List<Response> oldResponses = findResponsesByQuestionId(questionId);
        Iterator<Response> nri = newResponses.iterator();
        Iterator<Response> ori = oldResponses.iterator();

        // Update responses with matching questionId, save all that are new, delete all that are absent.
        while (nri.hasNext()) {
            Response newResponse = nri.next();
            while (ori.hasNext()) {
                Response oldResponse = ori.next();
                if (newResponse.getResponseId() == oldResponse.getResponseId()){
                    update(newResponse);
                    nri.remove();
                    ori.remove();
                    break;
                }
            }
        }
        for (Response r : oldResponses) {
            delete(r);
        }
        for (Response r : newResponses) {
            save(r);
        }
    }


    @Override
    public void delete(Response response) {
        try(Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM quiz.response WHERE response_id = ?;")) {

            statement.setInt(1, response.getResponseId());

            int result = statement.executeUpdate();
            if (result == 0) {
                throw new SQLException(String.format(
                        "No response with ID:%d was found", response.getResponseId()));
            }

        } catch (SQLException e) {
            log("DELETE failed", e);
        }
    }


    @Override
    public List<Response> findResponsesByQuestionId(int questionId) {

        List<Response> responses = new ArrayList<>();
        try(Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM quiz.response WHERE fk_question_id = ?;")) {

            statement.setInt(1, questionId);

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Response response = new Response(
                            rs.getInt("response_id"),
                            rs.getInt("fk_question_id"),
                            rs.getString("text"),
                            rs.getBoolean("correct")
                    );
                    responses.add(response);
                }
            }

        }  catch (SQLException e) {
            log("SELECT BY fk_question_id failed", e);
        }
        return responses;
    }

    private static void log(String msg, SQLException e) {
        LOGGER.log(Level.WARNING, String.format(
                "%s in DaoQuestionImpl \nSQLErrorCode: %d \nSQLState: %s", msg, e.getErrorCode(), e.getSQLState()), e);
    }

}
