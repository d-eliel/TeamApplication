package el.team_application;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by ariel-mac on 22/06/2015.
 */
public class TeamsApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        try {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
