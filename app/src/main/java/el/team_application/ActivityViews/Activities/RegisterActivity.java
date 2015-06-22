package el.team_application.ActivityViews.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import el.team_application.Listeners.User.AfterRegisterCallback;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;


public class RegisterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // view components:
        final Button registerButton   = (Button) findViewById(R.id.register_register_btn);
        final EditText emailText      = (EditText) findViewById(R.id.register_email_et);
        final EditText pwdText        = (EditText) findViewById(R.id.register_password_et);
        final EditText pwdConfText    = (EditText) findViewById(R.id.register_password_confirm_et);
        final EditText nameText       = (EditText) findViewById(R.id.register_name_et);
        final EditText phoneText      = (EditText) findViewById(R.id.register_phone_et);

        // listeners:
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = pwdText.getText().toString();
                String confirmPassword = pwdConfText.getText().toString();
                String email = emailText.getText().toString();

                // email validate
                if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailText.setError("Please Enter a Valid Email");
                    return;
                }
                // check if password and confirm password are equals
                if(!(password.equals(confirmPassword))){
                    pwdText.setError("Passwords Doesn't match");
                    return;
                }
                // check if passwords aren't empty
                if(password.isEmpty() || confirmPassword.isEmpty()){
                    pwdText.setError("Empty password is not allowed");
                    return;
                }

                // set new user object by EditText Fields
                String id = UUID.randomUUID().toString();
                String name = nameText.getText().toString();
                String phone = phoneText.getText().toString();
                User newUser = new User(id,email,name);
                newUser.setPhone(phone);

                // name validate
                if(name.isEmpty()){
                    nameText.setError("Please Enter Your Name");
                    return;
                }

                registerAsync(newUser, password);
            }
        });

    }

    // register async method
    private void registerAsync(final User user, String password) {
        Model.getInstance().register(user, password, new AfterRegisterCallback() {
            @Override
            public void registerSuccessful(User user) {
                Model.getInstance().setLoggedInUser(user);
                Toast.makeText(getApplicationContext(), "register successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void registerFailed(Exception e) {
                Toast.makeText(getApplicationContext(), "register failed: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                // todo - set error message for no internet
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
