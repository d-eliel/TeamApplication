package el.team_application.Models.Entities;

/**
 * Created by Eliel on 6/20/2015.
 */
public class Unsynced {
    String id;
    String action; // might change to enum
    String table; // might change to enum
    String changedObjectId;

    public Unsynced(String id, String action, String table, String changedObjectId) {
        this.id = id;
        this.action = action;
        this.table = table;
        this.changedObjectId = changedObjectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getChangedObjectId() {
        return changedObjectId;
    }

    public void setChangedObjectId(String changedObjectId) {
        this.changedObjectId = changedObjectId;
    }
}
