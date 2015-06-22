package el.team_application.ActivityViews.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import el.team_application.ActivityViews.Adapters.ViewPagerAdapter;
import el.team_application.ActivityViews.Tabs.SlidingTabLayout;
import el.team_application.Models.Model;
import el.team_application.R;

public class TeamActivity extends ActionBarActivity {
    /** STATIC VALUES **/
    public static final int TABS_NUMBER             = 4;
    public static final int NEW_TASK_REQUEST        = 10;
    public static final int NEW_TEAM_MEMBER_REQUEST = 20;

    // Declaring View and Variables
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]= {"Members","Team Tasks", "My Tasks", "Manage"};
    String currentTeamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Get team id from intent
        Intent intent = getIntent();
        currentTeamId = intent.getExtras().getString("id");

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,TABS_NUMBER,currentTeamId);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.team_pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.team_tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        pager.getCurrentItem();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        requestMyTeams();
        // Check which request we're responding to
        switch(requestCode){
            case NEW_TASK_REQUEST: {
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
                    adapter.notifyDataSetChanged();
                }
            }
            case NEW_TEAM_MEMBER_REQUEST: {
                if(resultCode == RESULT_OK) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team, menu);
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
        switch(id){
            case R.id.menu_team_members_action_new:
                Toast.makeText(getApplicationContext(), "New Team Member", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(),AddTeamMemberActivity.class);
                intent.putExtra("teamId", currentTeamId);
                startActivityForResult(intent, NEW_TEAM_MEMBER_REQUEST);
                return true;
            case R.id.menu_team_tasks_action_new:
                Toast.makeText(getApplicationContext(), "New Task", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(),AddTaskActivity.class);
                intent.putExtra("teamId", currentTeamId);
                startActivityForResult(intent, NEW_TASK_REQUEST);
                return true;
            case R.id.menu_team_action_exit:
                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_LONG).show();
                finish();
                System.exit(0);
            default:
                return false;
        }
//        return super.onOptionsItemSelected(item);
    }

}
