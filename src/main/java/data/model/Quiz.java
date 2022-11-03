package data.model;

import java.util.List;
import java.util.Set;

public class Quiz {

    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void addQuestions(Set<Question> questions) {
        this.questions.addAll(questions);
    }
}
