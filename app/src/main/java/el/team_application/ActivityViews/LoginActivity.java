package el.team_application.ActivityViews;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import el.team_application.Listeners.UserAuth.AfterLoginCallback;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;


public class LoginActivity extends ActionBarActivity {

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // buttons on screen:
        final Button button_login     = (Button) findViewById(R.id.login_login_btn);
        final Button button_reg       = (Button) findViewById(R.id.login_register_btn);
        final EditText emailText      = (EditText) findViewById(R.id.login_email_et);
        final EditText pwdText        = (EditText) findViewById(R.id.login_password_et);

        // click listeners:
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = pwdText.getText().toString();
                loginAsync(email,password);
            }
        });

        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // login async method
    public void loginAsync(String email, String password){
        Model.getInstance().login(email, password, new AfterLoginCallback() {
            @Override
            public void loginSuccessful(User user) {
                Model.getInstance().setLoggedInUser(user);
                Toast.makeText(getApplicationContext(),"login successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MyTeamsActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void loginFailed(Exception e) {
                Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void usernameOrPasswordIsInvalid(Exception e) {
                Toast.makeText(getApplicationContext(),"login failed - bad credentials",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
