package el.team_application.ActivityViews.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;

import el.team_application.Listeners.User.GetSessionCallback;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;

public class MainActivity extends ActionBarActivity {
    User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Parse Keys
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));

        final TextView userNameTV = (TextView) findViewById(R.id.main_username_tv);
        final Button go2InvitationsBtn = (Button) findViewById(R.id.main_invitation_btn);
        final Button go2MyTeamsBtn = (Button) findViewById(R.id.main_teams_btn);

        // getting the logged user on the device
        Model.getInstance().getSession(new GetSessionCallback() {
            @Override
            public void loggedInUser(User user) {
                if (user == null) {
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    userNameTV.setText(user.getName());
                    loggedInUser = user;
                    Model.getInstance().setLoggedInUser(user);
                }
            }
        });

        go2InvitationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InvitationsActivity.class);
                startActivity(intent);
            }
        });

        go2MyTeamsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyTeamsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.menu_my_teams_action_logoff:
                Toast.makeText(getApplicationContext(), "Logoff", Toast.LENGTH_LONG).show();
                Model.getInstance().logout();
                intent = new Intent(getApplicationContext(),WelcomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_my_teams_action_exit:
                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_LONG).show();
                finish();
                System.exit(1);
        }

        return super.onOptionsItemSelected(item);
    }
}
