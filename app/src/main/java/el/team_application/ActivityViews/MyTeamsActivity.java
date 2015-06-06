package el.team_application.ActivityViews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import el.team_application.Listeners.Teams.GetMyTeamsListener;
import el.team_application.Listeners.UserAuth.GetCurrentUserCallback;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;

public class MyTeamsActivity extends ActionBarActivity {
    ListView            listView;
    MyTeamsAdapter      adapter;
    User                loggedInUser;
    List<Team>          teams       = new LinkedList<Team>();
    ArrayList<String>   teamsNames  = new ArrayList<String>();
    static final int    NEW_TEAM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };
        for (int i = 0; i < values.length; ++i) {
            teamsNames.add(values[i]);
        }

        listView    = (ListView) findViewById(R.id.myteam_list);
        adapter     = new MyTeamsAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "item click " + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),TeamActivity.class);
                intent.putExtra("id",teamsNames.get(position));
                startActivity(intent);
            }
        });

        // getting current user
        Model.getInstance().getCurrentUser(new GetCurrentUserCallback() {
            @Override
            public void loggedInUser(User user) {
                if (user == null) {
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(intent);
                } else {
                    loggedInUser = user;
                    Model.getInstance().getMyTeams(user.getId(), new GetMyTeamsListener() {
                        @Override
                        public void onResult(List<Team> myTeams, Exception e) {
                            if (e != null) {
                                // todo print message to screen
                            } else {
                                teams = myTeams;
                                adapter.notifyDataSetChanged();

//                                for (Team team : teams){
//                                    teamsNames.add(team.getName());
//                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_teams, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.menu_my_teams_create_team:
                newTeam();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request we're responding to
        switch(requestCode){
            case NEW_TEAM_REQUEST: {
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
                    Model.getInstance().getMyTeams(loggedInUser.getId(), new GetMyTeamsListener() {
                        @Override
                        public void onResult(List<Team> myTeams, Exception e) {
                            if (e != null) {
                                // todo print message to screen
                            } else {
                                teams = myTeams;
                                adapter.notifyDataSetChanged();

                            }
                        }
                    });
                }
            }
        }
    }

    private void newTeam() {
        Toast.makeText(getApplicationContext(), "Create new Team", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),CreateTeamActivity.class);
        startActivityForResult(intent, NEW_TEAM_REQUEST);
    }

    class MyTeamsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return teamsNames.size();
        }

        @Override
        public Object getItem(int position) {
            return teamsNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.myteams_row_layout,null);
            }

            TextView teamName = (TextView) convertView.findViewById(R.id.myteams_row_name_tv);
            TextView managerName = (TextView) convertView.findViewById(R.id.myteams_row_manager_tv);
            convertView.setTag(position);

            String name = teamsNames.get(position);
            String manager = teamsNames.get(position)+" manager";
            teamName.setText(name);
            managerName.setText(manager);

            return convertView;
        }
    }

}
