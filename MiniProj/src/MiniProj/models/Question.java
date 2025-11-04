package MiniProj.models;

public class Question {
    public int id;
    public int quizId;
    public String text;
    public String a,b,c,d;
    public char correct;

    public Question(int id, int quizId, String text, String a, String b, String c, String d, char correct) {
        this.id = id;
        this.quizId = quizId;
        this.text = text;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correct = correct;
    }
}
