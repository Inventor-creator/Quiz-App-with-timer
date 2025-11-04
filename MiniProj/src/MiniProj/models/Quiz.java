package MiniProj.models;

public class Quiz {
    public int id;
    public String title;
    public String description;
    public int createdBy;

    public Quiz(int id, String title, String description, int createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return title + (description != null && !description.isEmpty() ? " - " + description : "");
    }
}
