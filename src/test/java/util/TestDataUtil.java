package util;

import data.eception.IncorrectQuestionException;
import data.eception.IncorrectResponseException;
import data.model.Question;
import data.model.Response;

import java.util.ArrayList;
import java.util.List;

public class TestDataUtil {

    public static final String TEST_TOPIC_1 = "Test Topic 1";
    public static final String TEST_TOPIC_2 = "Test Topic 2";
    public static final String TEST_CONTENT_1 = "Test Content 1";
    public static final String TEST_CONTENT_2 = "Test Content 2";

    public static Question mockQuestion() {
        try {
            Response response1 = new Response("Test Response 1", true);
            Response response2 = new Response("Test Response 2", false);
            Response response3 = new Response("Test Response 3", false);

            List<Response> responses = new ArrayList<>();
            responses.add(response1);
            responses.add(response2);
            responses.add(response3);

            return new Question(TEST_TOPIC_1, 5, TEST_CONTENT_1, responses);

        } catch (IncorrectQuestionException | IncorrectResponseException e) {
            throw new RuntimeException(e);
        }
    }
}
