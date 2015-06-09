package el.team_application.ActivityViews;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import el.team_application.Listeners.Members.AddMemberListener;
import el.team_application.Listeners.Teams.CreateTeamListener;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;

public class CreateTeamActivity extends ActionBarActivity {
    User loggedInUser = Model.getInstance().getLoggedInUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        final Button newTeamBtn     = (Button) findViewById(R.id.createteam_create_btn);
        final TextView nameTV       = (TextView) findViewById(R.id.createteam_name_et);
        final TextView managerTV    = (TextView) findViewById(R.id.createteam_manager_tv);

        managerTV.setText(loggedInUser.getEmail());

        newTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameTV.getText().toString();
                // Team name validation
                if(name.isEmpty()){
                    nameTV.setError("Please Enter Name");
                    return;
                }

                /* set new manager member */
                String memberId     = UUID.randomUUID().toString();
                String userId       = loggedInUser.getId();
                String now          = new Date().toString();
                String title        = "Manager of "+name;

                TeamMember manager  = new TeamMember (
                        memberId,
                        userId,
                        now,
                        title,
                        TeamMember.Role.MANAGER );

                List<TeamMember> membersList = new LinkedList<TeamMember>();
                membersList.add(manager);
                Team team = new Team(UUID.randomUUID().toString(), name, membersList);
                Model.getInstance().addMember(manager, new AddMemberListener() {
                    @Override
                    public void onResult(Exception e) {

                    }

                });
                Model.getInstance().createTeam(team, new CreateTeamListener() {
                    @Override
                    public void onResult(Exception e) {

                    }
                });

                /* move to the Team Activity */
                Intent intent = new Intent(getApplicationContext(),TeamActivity.class);
                intent.putExtra("id", team.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_team, menu);
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
