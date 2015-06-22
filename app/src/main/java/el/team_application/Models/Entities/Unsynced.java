package el.team_application.Models.Entities;

/**
 * Created by Eliel on 6/20/2015.
 */
public class Unsynced {
    public enum Action {
        CREATE,
        EDIT,
        REMOVE
    };

    public enum Table {
        USER,
        TEAM,
        MEMBER,
        TASK

    };

    String id;
    Action action;
    Table table;
    String changedObjectId;

    public Unsynced(String id, Action action, Table table, String changedObjectId) {
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getChangedObjectId() {
        return changedObjectId;
    }

    public void setChangedObjectId(String changedObjectId) {
        this.changedObjectId = changedObjectId;
    }

    @Override
    public boolean equals(Object obj){
        Unsynced unsync = (Unsynced) obj;
        if(this.getId().equals(unsync.getId())){
            return true;
        }else{
            return false;
        }
    }
}
