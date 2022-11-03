package data.model;

import data.eception.IncorrectQuestionException;
import data.eception.IncorrectResponseException;

import java.util.List;
import java.util.Objects;

public class Question {

    private int questionId;

    private String topic;

    private int difficultyRank;

    private String content;

    private List<Response> responses;

    public Question(String topic, int difficultyRank, String content, List<Response> responses)
            throws IncorrectResponseException, IncorrectQuestionException {
        this(0, topic, difficultyRank, content, responses);
    }

    public Question(int questionId, String topic, int difficultyRank, String content, List<Response> responses) {
        this.questionId = questionId;
        this.topic = topic;
        this.difficultyRank = difficultyRank;
        this.content = content;
        this.responses = responses;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getDifficultyRank() {
        return difficultyRank;
    }

    public void setDifficultyRank(int difficultyRank) {
        this.difficultyRank = difficultyRank;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question = (Question) o;
        return questionId == question.questionId
                && difficultyRank == question.difficultyRank
                && Objects.equals(topic, question.topic)
                && Objects.equals(content, question.content)
                && Objects.equals(responses, question.responses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, topic, difficultyRank, content, responses);
    }

    public static void validate(Question question) throws IncorrectQuestionException, IncorrectResponseException {
        if (question.getTopic() == null || question.getTopic().isBlank()) {
            throw new IncorrectQuestionException("Topic cannot be null nor blank");
        }
        if (question.getContent() == null || question.getContent().isBlank()) {
            throw new IncorrectQuestionException("Content cannot be null nor blank");
        }
        if (question.getResponses() == null || question.getResponses().size() == 1) {
            throw new IncorrectResponseException("Question must have at least 2 responses");
        }

        boolean hasCorrectResponse = false;
        for (Response r : question.getResponses()) {
            r.setQuestionId(question.getQuestionId());

            if (r.getText() == null || r.getText().isBlank()) {
                throw new IncorrectResponseException("Response text must not null nor blank");
            }
            if (r.isCorrect()) {
                hasCorrectResponse = true;
            }
        }
        if (!hasCorrectResponse){
            throw new IncorrectResponseException("Question must have at least one correct response");
        }
    }
}
