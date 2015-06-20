package el.team_application.Models.ListItems;

public class TaskMembersListItem {
    String id;
    String name;
    String title;

    public TaskMembersListItem(String id, String name, String jobTitle) {
        this.id = id;
        this.name = name;
        this.title = jobTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
            this.title = title;
        }
}