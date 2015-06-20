package el.team_application.Listeners.Invitations;

import java.util.List;

import el.team_application.Models.Entities.Invitation;

/**
 * Created by Eliel on 6/19/2015.
 */
public interface GetInvitationsForUserListener {
    void onResult(List<Invitation> invitations, Exception e);
}
