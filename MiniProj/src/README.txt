QuizAppSplit - Java Swing + PostgreSQL (split-files)

How to compile & run (Linux/macOS):
1. Ensure PostgreSQL is running and a database named JavaMiniProj exists:
   createdb JavaMiniProj
   (or create it in pgAdmin)

2. Place the PostgreSQL JDBC driver JAR (e.g., postgresql-42.5.4.jar) somewhere accessible.

3. Compile (from project root where Main.java is):
   javac -cp path/to/postgresql-<version>.jar Main.java MiniProj.DBUtil.java MiniProj.ui.models/*.java MiniProj.ui/*.java

4. Run:
   java -cp .:path/to/postgresql-<version>.jar Main
   (On Windows replace ':' with ';')

Database settings:
- URL: jdbc:postgresql://localhost:5432/JavaMiniProj
- User: postgres
- Password: postgres

Notes:
- Students and instructors create accounts by entering a username and selecting a role.
- Instructor can create quizzes and view student marks.
- Basic demo project; not production-ready.
