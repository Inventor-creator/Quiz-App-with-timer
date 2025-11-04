package MiniProj;

import MiniProj.models.Attempt;
import MiniProj.models.Question;
import MiniProj.models.Quiz;
import MiniProj.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private static Connection conn;

    public static void init(String url, String user, String pass) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Postgres JDBC Driver not found on classpath.");
        }
        conn = DriverManager.getConnection(url, user, pass);
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void initTables() throws SQLException {
        String users = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username TEXT UNIQUE NOT NULL, role TEXT NOT NULL CHECK (role IN ('student','instructor')));";
        String quizzes = "CREATE TABLE IF NOT EXISTS quizzes (id SERIAL PRIMARY KEY, title TEXT NOT NULL, description TEXT, created_by INTEGER REFERENCES users(id), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        String questions = "CREATE TABLE IF NOT EXISTS questions (id SERIAL PRIMARY KEY, quiz_id INTEGER REFERENCES quizzes(id) ON DELETE CASCADE, question_text TEXT NOT NULL, option_a TEXT, option_b TEXT, option_c TEXT, option_d TEXT, correct CHAR(1));";
        String attempts = "CREATE TABLE IF NOT EXISTS attempts (id SERIAL PRIMARY KEY, student_id INTEGER REFERENCES users(id), quiz_id INTEGER REFERENCES quizzes(id), score INTEGER, total INTEGER, attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        try (Statement st = conn.createStatement()) {
            st.execute(users);
            st.execute(quizzes);
            st.execute(questions);
            st.execute(attempts);
        }
    }

    // -- convenience wrappers used by UI --

    public static User ensureUserExists(String username, String role) throws SQLException {
        String q = "SELECT id, username, role FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
        }

        String ins = "INSERT INTO users (username, role) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(ins)) {
            ps.setString(1, username);
            ps.setString(2, role);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(rs.getInt(1), username, role);
        }
        throw new SQLException("Failed to ensure user exists");
    }

    public static List<Quiz> getAllQuizzes() throws SQLException {
        List<Quiz> out = new ArrayList<>();
        String q = "SELECT id, title, description, created_by FROM quizzes ORDER BY created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) out.add(new Quiz(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getInt("created_by")));
        }
        return out;
    }

    public static int createQuiz(String title, String desc, int createdBy) throws SQLException {
        String q = "INSERT INTO quizzes (title, description, created_by) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setInt(3, createdBy);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        throw new SQLException("Failed to create quiz");
    }

    public static void addQuestion(int quizId, String text, String a, String b, String c, String d, char correct) throws SQLException {
        String q = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, quizId);
            ps.setString(2, text);
            ps.setString(3, a);
            ps.setString(4, b);
            ps.setString(5, c);
            ps.setString(6, d);
            ps.setString(7, String.valueOf(correct));
            ps.executeUpdate();
        }
    }

    public static java.util.List<Question> getQuestionsForQuiz(int quizId) throws SQLException {
        java.util.List<Question> out = new ArrayList<>();
        String q = "SELECT id, question_text, option_a, option_b, option_c, option_d, correct FROM questions WHERE quiz_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new Question(rs.getInt("id"), quizId, rs.getString("question_text"), rs.getString("option_a"), rs.getString("option_b"), rs.getString("option_c"), rs.getString("option_d"), rs.getString("correct") != null ? rs.getString("correct").charAt(0) : ' '));
            }
        }
        return out;
    }

    public static void saveAttempt(int studentId, int quizId, int score, int total) throws SQLException {
        String q = "INSERT INTO attempts (student_id, quiz_id, score, total) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, studentId);
            ps.setInt(2, quizId);
            ps.setInt(3, score);
            ps.setInt(4, total);
            ps.executeUpdate();
        }
    }

    public static java.util.List<Attempt> getAttemptsForQuiz(int quizId) throws SQLException {
        java.util.List<Attempt> out = new ArrayList<>();
        String q = "SELECT a.id, a.student_id, u.username, a.score, a.total, a.attempted_at FROM attempts a JOIN users u ON a.student_id = u.id WHERE a.quiz_id = ? ORDER BY a.attempted_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new Attempt(rs.getInt("id"), rs.getInt("student_id"), rs.getString("username"), quizId, rs.getInt("score"), rs.getInt("total"), rs.getTimestamp("attempted_at").toLocalDateTime()));
            }
        }
        return out;
    }

    public static java.util.List<User> getAllStudents() throws SQLException {
        java.util.List<User> out = new ArrayList<>();
        String q = "SELECT id, username, role FROM users WHERE role = 'student' ORDER BY username";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) out.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("role")));
        }
        return out;
    }
}
