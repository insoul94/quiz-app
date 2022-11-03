package data.model;

import data.eception.IncorrectResponseException;

import java.util.Objects;

public class Response {

    private int responseId;

    private int questionId;

    private String text;

    private boolean correct;

    public Response(String text, boolean correct) {
        this(0, 0, text, correct);
    }

    public Response(int questionId, String text, boolean correct) {
        this(0, questionId, text, correct);
    }

    public Response(int responseId, int questionId, String text, boolean correct) {
        this.responseId = responseId;
        this.questionId = questionId;
        this.text = text;
        this.correct = correct;
    }

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;
        Response response = (Response) o;
        return responseId == response.responseId
                && questionId == response.questionId
                && correct == response.correct
                && Objects.equals(text, response.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseId, questionId, text, correct);
    }
}
