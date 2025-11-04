package MiniProj.models;

import java.time.LocalDateTime;

public class Attempt {
    public int id;
    public int studentId;
    public String username;
    public int quizId;
    public int score;
    public int total;
    public LocalDateTime when;

    public Attempt(int id, int studentId, String username, int quizId, int score, int total, LocalDateTime when) {
        this.id = id;
        this.studentId = studentId;
        this.username = username;
        this.quizId = quizId;
        this.score = score;
        this.total = total;
        this.when = when;
    }
}
