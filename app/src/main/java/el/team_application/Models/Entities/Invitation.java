package el.team_application.Models.Entities;

/**
 * Created by Eliel on 6/19/2015.
 */
public class Invitation {
    String id;
    String inviteeUserId;
    String inviterUserId;
    String inviterEmail;
    String inviterName;
    String teamId;
    String teamName;
    String inviteeTitle;
    boolean accepted;

    public Invitation(String id, String inviteeUserId, String inviterUserId, String inviterEmail, String inviterName, String teamId, String teamName, String inviteeTitle, boolean accepted) {
        this.id = id;
        this.inviteeUserId = inviteeUserId;
        this.inviterUserId = inviterUserId;
        this.inviterEmail = inviterEmail;
        this.inviterName = inviterName;
        this.teamId = teamId;
        this.teamName = teamName;
        this.inviteeTitle = inviteeTitle;
        this.accepted = accepted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInviteeUserId() {
        return inviteeUserId;
    }

    public void setInviteeUserId(String inviteeUserId) {
        this.inviteeUserId = inviteeUserId;
    }

    public String getInviterUserId() {
        return inviterUserId;
    }

    public void setInviterUserId(String inviterUserId) {
        this.inviterUserId = inviterUserId;
    }

    public String getInviterEmail() {
        return inviterEmail;
    }

    public void setInviterEmail(String inviterEmail) {
        this.inviterEmail = inviterEmail;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getInviteeTitle() {
        return inviteeTitle;
    }

    public void setInviteeTitle(String inviteeTitle) {
        this.inviteeTitle = inviteeTitle;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

}
