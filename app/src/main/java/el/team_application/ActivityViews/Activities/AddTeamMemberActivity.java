package el.team_application.ActivityViews.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import el.team_application.Listeners.Invitations.NewInviteListener;
import el.team_application.Listeners.Teams.EditTeamListener;
import el.team_application.Listeners.Teams.GetTeamByIdListener;
import el.team_application.Listeners.User.GetTeamUsersCallback;
import el.team_application.Listeners.User.GetUserByEmailCallback;
import el.team_application.Listeners.User.JoinUserToTeamCallback;
import el.team_application.Models.Entities.Invitation;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;

public class AddTeamMemberActivity extends ActionBarActivity {
    User loggedInUser         = Model.getInstance().getLoggedInUser();
    private Team currentTeam; // current team - the team we add a member to
    private List<User> currentUsers; // User objects of the current team

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_member);

        final Button newMemberBtn = (Button) findViewById(R.id.add_team_member_join_btn);
        final EditText emailET    = (EditText) findViewById(R.id.add_team_member_email);
        final EditText titleET    = (EditText) findViewById(R.id.add_team_member_title);

        // Get team id from intent
        Intent getIntent = getIntent();
        final String teamid = getIntent.getExtras().getString("teamId");

        /* get the current team object */
        Model.getInstance().getTeamById(teamid, new GetTeamByIdListener() {
            @Override
            public void onResult(Team team, Exception e) {
                currentTeam = team;
            }
        });

        /* get User Objects for current team */
        Model.getInstance().getTeamUsers(teamid, new GetTeamUsersCallback() {
            @Override
            public void onResult(List<User> users, Exception e) {
                currentUsers = users;
            }
        });

        newMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailET.getText().toString();
                final String title = titleET.getText().toString();

                // email validate
                if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailET.setError("Please Enter a Valid Email");
                    return;
                }

                if(userEmailExistInTeamMembers(email)){
                    emailET.setError("This User already in team");
                    return;
                }

                Model.getInstance().getUserByEmail(email, new GetUserByEmailCallback() {
                    @Override
                    public void onResult(User user, Exception e) {
                        Invitation invitation = new Invitation(UUID.randomUUID().toString(),
                                user.getId(),
                                loggedInUser.getId(),
                                loggedInUser.getEmail(),
                                loggedInUser.getName(),
                                currentTeam.getId(),
                                currentTeam.getName(),
                                title,
                                false);

                        Model.getInstance().newInvite(invitation, new NewInviteListener() {
                            @Override
                            public void onResult(Exception e) {
                                Toast.makeText(getApplicationContext(), "New Invitation Sent", Toast.LENGTH_LONG).show();
                                Intent resultIntent = new Intent();
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        });
                    }
                });
            }
        });

    }

    private boolean userEmailExistInTeamMembers(String email) {
        for(TeamMember member : currentTeam.getMemberList()){
            for(User user : currentUsers){
                if(user.getId().equals(member.getUserId())){
                    if(user.getEmail().equals(email)){
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_team_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
