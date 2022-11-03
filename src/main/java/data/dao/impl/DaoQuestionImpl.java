package data.dao.impl;

import data.eception.IncorrectQuestionException;
import data.eception.IncorrectResponseException;
import data.util.DBUtils;
import data.dao.DaoQuestion;
import data.dao.DaoResponse;
import data.model.Question;
import data.model.Response;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoQuestionImpl implements DaoQuestion {

    private static final Logger LOGGER = Logger.getLogger(DaoQuestionImpl.class.getName());
    private final DaoResponse daoResponse;

    public DaoQuestionImpl() {
        daoResponse = new DaoResponseImpl();
    }


    public void save(Question question) throws IncorrectQuestionException, IncorrectResponseException {

        try(Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO quiz.question (topic, difficulty_rank, content) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS)) {

            Question.validate(question);
            statement.setString(1, question.getTopic());
            statement.setInt(2, question.getDifficultyRank());
            statement.setString(3, question.getContent());

            int result = statement.executeUpdate();
            if (result == 0) {
                throw new SQLException();
            }

            try (ResultSet keys = statement.getGeneratedKeys()){
                if (keys.next()) {
                    int questionId = keys.getInt(1);
                    question.setQuestionId(questionId);

                    for (Response r : question.getResponses()) {
                        r.setQuestionId(questionId);
                        daoResponse.save(r);
                    }
                }
            }

        } catch (SQLException e) {
            log("SAVE failed", e);
        }
    }


    public void update(Question question)
            throws IncorrectQuestionException, IncorrectResponseException {

        try(Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE quiz.question SET topic = ?, difficulty_rank = ?, content = ? WHERE question_id = ?;")) {

            Question.validate(question);
            statement.setString(1, question.getTopic());
            statement.setInt(2, question.getDifficultyRank());
            statement.setString(3, question.getContent());
            statement.setInt(4, question.getQuestionId());

            int result = statement.executeUpdate();
            if (result == 0) {
                throw new SQLException(String.format(
                        "No question with ID:%d was found", question.getQuestionId()));
            }

            daoResponse.updateAllByQuestionId(question.getResponses(), question.getQuestionId());

        } catch (SQLException e) {
            log("UPDATE failed", e);
        }
    }


    public void delete(Question question) {
        try(Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM quiz.question WHERE question_id = ?;")) {

            statement.setInt(1, question.getQuestionId());

            int result = statement.executeUpdate();
            if (result == 0) {
                throw new SQLException(String.format(
                        "No question with ID:%d was found", question.getQuestionId()));
            }

        } catch (SQLException e) {
            log("DELETE failed", e);
        }
    }


    public List<Question> findByTopic(String topic)
            throws IncorrectQuestionException, IncorrectResponseException {

        List<Question> questions = new ArrayList<>();

        try(Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM quiz.question WHERE topic = ?;", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, topic);

            try(ResultSet rs = statement.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    throw new SQLException(String.format(
                            "No questions with topic {%s} was found", topic));
                }

                while (rs.next()) {
                    int questionId = rs.getInt("question_id");
                    Question question = new Question(
                            questionId,
                            rs.getString("topic"),
                            rs.getInt("difficulty_rank"),
                            rs.getString("content"),
                            daoResponse.findResponsesByQuestionId(questionId));

                    Question.validate(question);
                    questions.add(question);
                }
            }
        }  catch (SQLException e) {
            log("SELECT BY TOPIC failed", e);
        }
        return questions;
    }

    private static void log(String msg, SQLException e) {
        LOGGER.log(Level.WARNING, String.format(
                "%s in DaoQuestionImpl \nSQLErrorCode: %d \nSQLState: %s", msg, e.getErrorCode(), e.getSQLState()), e);
    }
}
