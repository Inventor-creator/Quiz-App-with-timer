package MiniProj.models;

public class Question {
    private int id;
    private int quizId;
    private String text;
    private String a;
    private String b;
    private String c;
    private String d;
    private char correct;
    private char userAnswer = '\0';

    public Question(int id, int quizId, String text,
                    String a, String b, String c, String d, char correct) {
        this.id = id;
        this.quizId = quizId;
        this.text = text;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public int getQuizId() {
        return quizId;
    }

    public String getText() {
        return text;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public char getCorrect() {
        return correct;
    }

    public char getUserAnswer() {
        return userAnswer;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setA(String a) {
        this.a = a;
    }

    public void setB(String b) {
        this.b = b;
    }

    public void setC(String c) {
        this.c = c;
    }

    public void setD(String d) {
        this.d = d;
    }

    public void setCorrect(char correct) {
        this.correct = correct;
    }

    public void setUserAnswer(char userAnswer) {
        this.userAnswer = userAnswer;
    }
}
