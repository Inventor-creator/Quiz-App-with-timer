
CREATE TABLE Students(
	studentId INTEGER,
	PRIMARY KEY(studentId)
);

CREATE TABLE Quizzes(
	quizId INTEGER,
	quizName VARCHAR(20),
	PRIMARY KEY(quizId)
);

CREATE TABLE Questions(
	questionId INTEGER,
	quizId INTEGER,
	FOREIGN KEY (quizId) REFERENCES Quizzes(quizId),
	PRIMARY KEY (questionId)
	
);


CREATE TABLE StudentMarks(
	studentId INTEGER,
	quizId INTEGER,
	FOREIGN KEY (quizId) REFERENCES Quizzes(quizId),
	FOREIGN KEY (studentId) REFERENCES Students(studentId)
);	



DROP TABLE Students;
DROP TABLE Quizzes;
DROP TABLE Questions;
DROP TABLE StudentMarks;


