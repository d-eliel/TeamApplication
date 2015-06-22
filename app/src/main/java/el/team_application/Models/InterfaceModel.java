package el.team_application.Models;

import android.content.Context;

import java.util.List;

import el.team_application.Listeners.Invitations.AcceptInviteListener;
import el.team_application.Listeners.Invitations.DeclineInviteListener;
import el.team_application.Listeners.Invitations.GetInvitationsForUserListener;
import el.team_application.Listeners.Invitations.NewInviteListener;
import el.team_application.Listeners.Members.AddMemberListener;
import el.team_application.Listeners.Members.EditMemberListener;
import el.team_application.Listeners.Members.GetAllTeamMembersListener;
import el.team_application.Listeners.Members.GetMemberByIdListener;
import el.team_application.Listeners.Members.GetMembersForTaskCallback;
import el.team_application.Listeners.Members.RemoveMemberListener;
import el.team_application.Listeners.Tasks.AddTaskListener;
import el.team_application.Listeners.Tasks.EditTaskListener;
import el.team_application.Listeners.Tasks.GetTaskListener;
import el.team_application.Listeners.Tasks.RemoveTaskListener;
import el.team_application.Listeners.Teams.CreateTeamListener;
import el.team_application.Listeners.Teams.EditTeamListener;
import el.team_application.Listeners.Teams.GetMyTeamsListener;
import el.team_application.Listeners.Teams.GetTeamByIdListener;
import el.team_application.Listeners.Teams.RemoveTeamListener;
import el.team_application.Listeners.User.AfterLoginCallback;
import el.team_application.Listeners.User.AfterRegisterCallback;
import el.team_application.Listeners.User.GetSessionCallback;
import el.team_application.Listeners.User.GetSessionUserCallback;
import el.team_application.Listeners.User.GetTeamUsersCallback;
import el.team_application.Listeners.User.GetUserByEmailCallback;
import el.team_application.Listeners.User.GetUsersToAddCallback;
import el.team_application.Listeners.User.JoinUserToTeamCallback;
import el.team_application.Models.Entities.Invitation;
import el.team_application.Models.Entities.Session;
import el.team_application.Models.Entities.Task;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.Unsynced;
import el.team_application.Models.Entities.User;

/**
 * Created by Eliel on 6/21/2015.
 */
public interface InterfaceModel {
    void init(Context context);

    void emptyTables();
    //region Team CRUD
    void createTeam(Team team, CreateTeamListener createTeamListener);
    void createTeamSync(Team team);
    void editTeam(Team team, EditTeamListener editTeamListener);
    void editTeamSync(Team team);
    void removeTeam(Team team, RemoveTeamListener removeTeamListener);
    void getTeamById(String id, GetTeamByIdListener getTeamByIdListener);
    Team getTeamByIdSync(String id);
    void getMyTeams(String userId, GetMyTeamsListener listener);
    //endregion

    //region TeamMember CRUD
    void addMember(TeamMember member, AddMemberListener addMemberListener);
    void editMember(TeamMember member, EditMemberListener editMemberListener);
    void removeMember(TeamMember member, RemoveMemberListener removeMemberListener);
    void getMemberById(String id, GetMemberByIdListener getMemberByIdListener);
    void getMembersForTeam(String teamId, GetAllTeamMembersListener getAllTeamMembersListener);
    void getMembersForTask(String taskId, GetMembersForTaskCallback getMembersForTaskCallback);          // TODO : add callback?
    //endregion

    //region Task CRUD
    void addTask(Task task, AddTaskListener addTaskListener);
    void editTask(Task task, EditTaskListener editTaskListener);
    void removeTask(Task task, RemoveTaskListener removeTaskListener);
    void getTaskById(String taskId, GetTaskListener getTaskListener);
    void getTeamTasks(String teamId);               // TODO : add callback?
    void getUserTasks(String userId);               // TODO : add callback?
    void getTeamMemberTasks(String teamMemberId);   // TODO : add callback?
    //endregion

    //region Invitation CRUD
    void newInvite(Invitation invitation, NewInviteListener newInviteListener);
    void acceptInvite(Invitation invitation, AcceptInviteListener acceptInviteListener);
    void declineInvite(Invitation invitation, DeclineInviteListener declineInviteListener);
    void getInvitationsForUser(String userId, GetInvitationsForUserListener getInvitationsListener);
    //endregion

    //region User CRUD & Authentication
    void logout();
    void getSession(GetSessionCallback callback);    // get current user session)
    void getSessionUser(String userId, String parseToken, GetSessionUserCallback callback);
    void setSessionUser(User user);
    void setSession(Session session);
    void login(String email, String password, AfterLoginCallback loginCallback);
    void register(User user, String password, AfterRegisterCallback registerCallback);
    void joinUserToTeam(String userid, String teamId, JoinUserToTeamCallback joinUserToTeamCallback);
    void getUserByEmail(String email, GetUserByEmailCallback getUserByEmailCallback);
    void getUsersToAdd(String teamId, GetUsersToAddCallback getUsersToAddCallback);
    void getTeamUsers(String teamId, GetTeamUsersCallback getTeamUsersCallback);
    //endregion

    // TODO : add callbacks
    //region SYNC - for SQL push
    void addUnsynced(Unsynced unsynced);
    void removeUnsynced(String unsyncedId);
    List<Unsynced> getUnsynced();
    //endregion
}
