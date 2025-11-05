package MiniProj.models;

import java.time.LocalDateTime;

public class Attempt {
    private int id;
    private int studentId;
    private String studentName;
    private int quizId;
    private int score;
    private int total;
    private LocalDateTime when;

    public Attempt(int id, int studentId, String studentName,
                   int quizId, int score, int total, LocalDateTime when) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.quizId = quizId;
        this.score = score;
        this.total = total;
        this.when = when;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getQuizId() {
        return quizId;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

    @Override
    public String toString() {
        return String.format("Attempt[quizId=%d, score=%d/%d, when=%s]",
                quizId, score, total, when);
    }
}
