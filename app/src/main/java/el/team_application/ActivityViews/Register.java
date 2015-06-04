package el.team_application.ActivityViews;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import el.team_application.Listeners.AfterRegisterCallback;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;


public class Register extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // view components:
        final Button registerButton   = (Button) findViewById(R.id.register_register_btn);
        final EditText emailText      = (EditText) findViewById(R.id.register_email_et);
        final EditText pwdText        = (EditText) findViewById(R.id.register_password_et);
        final EditText pwdConfText    = (EditText) findViewById(R.id.register_password_confirm_et);
        final EditText errorsText     = (EditText) findViewById(R.id.register_password_confirm_et);

        // listeners:
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = pwdText.getText().toString();
                String confirmPassword = pwdConfText.getText().toString();
                if(!(password.equals(confirmPassword))){
                    errorsText.setText("Passwords Doesn't Match");
                    errorsText.setVisibility(View.VISIBLE);
                    return;
                }
                registerAsync(email, password);
            }
        });

        pwdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorsText.setText("");
                errorsText.setVisibility(View.GONE);
            }
        });

    }

    // register async method
    private void registerAsync(String email, String password) {
        Model.getInstance().register(new AfterRegisterCallback() {
            @Override
            public void registerSuccessful(User user) {
                // todo move to app activity - teams view
                Toast.makeText(getApplicationContext(), "register successful", Toast.LENGTH_LONG).show();
            }

            @Override
            public void registerFailed(Exception e) {
                Toast.makeText(getApplicationContext(), "register failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void usernameOrPasswordIsInvalid(Exception e) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
