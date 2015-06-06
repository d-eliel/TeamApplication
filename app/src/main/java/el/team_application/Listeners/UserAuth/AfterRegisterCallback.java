package el.team_application.Listeners.UserAuth;

/**
 * Created by Eliel on 6/4/2015.
 */
public interface AfterRegisterCallback {
    void registerSuccessful();
    void registerFailed(Exception e);
}
