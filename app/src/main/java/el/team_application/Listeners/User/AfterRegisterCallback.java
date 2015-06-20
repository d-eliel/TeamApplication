package el.team_application.Listeners.User;

/**
 * Created by Eliel on 6/4/2015.
 */
public interface AfterRegisterCallback {
    void registerSuccessful();
    void registerFailed(Exception e);
}
