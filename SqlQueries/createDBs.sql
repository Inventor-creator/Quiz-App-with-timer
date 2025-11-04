CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    studentname VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE quizzes (
    id SERIAL PRIMARY KEY,
    quizname VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    quiz_id INT REFERENCES quizzes(id) ON DELETE CASCADE,
    question TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    correct_option CHAR(1) NOT NULL
);

CREATE TABLE results (
    id SERIAL PRIMARY KEY,
    student_id INT REFERENCES students(id) ON DELETE CASCADE,
    quiz_id INT REFERENCES quizzes(id) ON DELETE CASCADE,
    score INT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



DROP TABLE marks;
DROP TABLE Questions;
DROP TABLE Students;
DROP TABLE Quizzes;



select * from students;
select * from quizzes;

