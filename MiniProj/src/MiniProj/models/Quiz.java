package MiniProj.models;

public class Quiz {
    private int id;
    private String title;
    private String description;
    private int createdBy;

    public Quiz(int id, String title, String description, int createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getCreatedBy() { return createdBy; }

    @Override
    public String toString() {
        return title + (description != null && !description.isEmpty() ? " — " + description : "");
    }
}
