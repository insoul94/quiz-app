package data.dao.impl;

import util.TestDataUtil;
import data.dao.DaoQuestion;
import data.eception.IncorrectQuestionException;
import data.eception.IncorrectResponseException;
import data.model.Question;
import data.model.Response;
import data.util.DBUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.TestDataUtil.*;


class DaoQuestionImplTest {

    private DaoQuestion daoQuestion;

    @BeforeAll
    static void init() throws FileNotFoundException, SQLException {
        try(Connection conn = DBUtils.getHostConnection()) {
            ScriptRunner sr = new ScriptRunner(conn);
            Reader reader = new BufferedReader(new FileReader("src/main/resources/schema.sql"));
            // Run schema.sql DB initialization script.
            sr.runScript(reader);
        }
    }

    @AfterAll
    static void destroy() throws FileNotFoundException, SQLException {
        try (Connection conn = DBUtils.getHostConnection()) {
            ScriptRunner sr = new ScriptRunner(conn);
            Reader reader = new BufferedReader(new FileReader("src/main/resources/drop.sql"));
            // Run drop.sql DB destruction script.
            sr.runScript(reader);
        }
    }

    @BeforeEach
    void setUp() {
        daoQuestion = new DaoQuestionImpl();
    }


    @Test
    void Should_Create_When_Save_Given_Question() {
        // GIVEN
        Question question = mockQuestion();
        try {
            // WHEN
            daoQuestion.save(question);

            // THEN
            List<Question> questions = daoQuestion.findByTopic(TEST_TOPIC_1);
            assertTrue(questions.contains(question));

            // CLEAN-UP
            daoQuestion.delete(question);

        } catch (IncorrectQuestionException | IncorrectResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void Should_ThrowIncorrectQuestionException_When_Save_Given_QuestionWithBlankTopic() {
        // GIVEN
        Question question = mockQuestion();
        question.setTopic("");

        // WHEN, THEN
        assertThrows(IncorrectQuestionException.class, () -> {
            daoQuestion.save(question);
        });
    }

    @Test
    void Should_ThrowIncorrectQuestionException_When_Save_Given_QuestionWithBlankContent() {
        // GIVEN
        Question question = mockQuestion();
        question.setTopic("");

        // WHEN, THEN
        assertThrows(IncorrectQuestionException.class, () -> {
            daoQuestion.save(question);
        });
    }

    @Test
    void Should_ThrowIncorrectResponseException_When_Save_Given_QuestionWithNullResponses() {
        // GIVEN
        Question question = mockQuestion();
        question.setResponses(null);

        // WHEN, THEN
        assertThrows(IncorrectResponseException.class, () -> {
            daoQuestion.save(question);
        });
    }

    @Test
    void Should_ThrowIncorrectResponseException_When_Save_Given_QuestionWithNoCorrectResponse() {
        // GIVEN
        Question question = mockQuestion();
        for (Response r : question.getResponses()) {
            r.setCorrect(false);
        }

        // WHEN, THEN
        assertThrows(IncorrectResponseException.class, () -> {
            daoQuestion.save(question);
        });
    }

    @Test
    void Should_ThrowIncorrectResponseException_When_Save_Given_QuestionWithBlankResponse() {
        // GIVEN
        Question question = mockQuestion();
        question.getResponses().get(0).setText("");

        // WHEN, THEN
        assertThrows(IncorrectResponseException.class, () -> {
            daoQuestion.save(question);
        });
    }

    @Test
    void Should_ThrowIncorrectResponseException_When_Save_Given_QuestionWithOneResponse() {
        // GIVEN
        Question question = mockQuestion();
        Response response1 = new Response("Test Response 1", true);
        List<Response> responses = new ArrayList<>();
        responses.add(response1);
        question.setResponses(responses);

        // WHEN, THEN
        assertThrows(IncorrectResponseException.class, () -> {
            daoQuestion.save(question);
        });
    }

    @Test
    void Should_UpdateCascade_When_Update_Given_QuestionExist() {
        // GIVEN
        Question question = mockQuestion();
        try {
            daoQuestion.save(question);
            question.setContent(TEST_CONTENT_2);
            question.setTopic(TEST_TOPIC_2);
            question.setDifficultyRank(4);
            question.getResponses().get(0).setCorrect(false);
            question.getResponses().get(1).setCorrect(false);
            question.getResponses().get(2).setCorrect(true);

            // WHEN
            daoQuestion.update(question);

            // THEN
            List<Question> questions = daoQuestion.findByTopic(TEST_TOPIC_2);
            assertTrue(questions.contains(question));

            // CLEAN-UP
            daoQuestion.delete(question);

        } catch (IncorrectQuestionException | IncorrectResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void Should_Remove_When_Delete_Given_QuestionExists() {
        // GIVEN
        Question question = TestDataUtil.mockQuestion();
        try {
            daoQuestion.save(question);

            // WHEN
            daoQuestion.delete(question);

            // THEN
            List<Question> questions = daoQuestion.findByTopic(TestDataUtil.TEST_TOPIC_1);
            assertFalse(questions.contains(question));

        } catch (IncorrectQuestionException | IncorrectResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void Should_ReturnQuestions_When_FindQuestionsByTopic_Given_QuestionsExistAndCorrectTopic() {
        // GIVEN
        Question question1 = TestDataUtil.mockQuestion();
        Question question2 = TestDataUtil.mockQuestion();
        List<Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);
        try {
            daoQuestion.save(question1);
            daoQuestion.save(question2);

            // WHEN
            List<Question> questionsReceived = daoQuestion.findByTopic(TEST_TOPIC_1);

            // THEN
            assertTrue(questionsReceived.containsAll(questions));

            // CLEAN-UP
            daoQuestion.delete(question1);
            daoQuestion.delete(question2);

        } catch (IncorrectQuestionException | IncorrectResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void Should_ReturnEmptyList_When_FindQuestionsByTopic_Given_QuestionsNotExist() {
        // WHEN, THEN
        try {
            assertEquals(0, daoQuestion.findByTopic(TEST_TOPIC_1).size());
        } catch (IncorrectQuestionException | IncorrectResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void Should_ReturnEmptyList_When_FindQuestionsByTopic_Given_TopicNotExist() {
        // GIVEN
        Question question1 = TestDataUtil.mockQuestion();
        try {
            daoQuestion.save(question1);

            // WHEN, THEN
            assertEquals(0, daoQuestion.findByTopic(TEST_TOPIC_2).size());

            // CLEAN-UP
            daoQuestion.delete(question1);

        } catch (IncorrectQuestionException | IncorrectResponseException e) {
            throw new RuntimeException(e);
        }
    }
}