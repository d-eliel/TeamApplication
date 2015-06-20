package el.team_application.Listeners.Tasks;

import el.team_application.Models.Entities.Task;

/**
 * Created by Eliel on 6/18/2015.
 */
public interface GetTaskListener {
    void onResult(Task task, Exception e);
}
