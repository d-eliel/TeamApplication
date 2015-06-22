package el.team_application.Models.Entities;

/**
 * Created by Eliel on 6/21/2015.
 */
public class Session {
    String sessionId;
    String userId;
    String token;
    boolean isLogged;
    boolean isUnsynced;
    String lastSyncFromServer;

    public Session(String sessionId, String userId, String token, boolean isLogged, boolean isUnsynced, String lastSyncFromServer) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.token = token;
        this.isLogged = isLogged;
        this.isUnsynced = isUnsynced;
        this.lastSyncFromServer = lastSyncFromServer;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public boolean isUnsynced() {
        return isUnsynced;
    }

    public void setIsUnsynced(boolean isUnsynced) {
        this.isUnsynced = isUnsynced;
    }

    public String getLastSyncFromServer() {
        return lastSyncFromServer;
    }

    public void setLastSyncFromServer(String lastSyncFromServer) {
        this.lastSyncFromServer = lastSyncFromServer;
    }
}
