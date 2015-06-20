package el.team_application.ActivityViews.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import el.team_application.Listeners.Tasks.AddTaskListener;
import el.team_application.Listeners.Teams.EditTeamListener;
import el.team_application.Listeners.Teams.GetTeamByIdListener;
import el.team_application.Listeners.User.GetTeamUsersCallback;
import el.team_application.Models.Entities.Task;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;

public class AddTaskActivity extends ActionBarActivity {
    User loggedInUser = Model.getInstance().getLoggedInUser();
    TeamMember currentTeamMember;
    Team currentTeam;
    ListView listView;
    CurrentMembersAdapter adapter;
    List<User> currentUsers = new LinkedList<>();
    List<String> taskMembersIds = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Get team id from intent
        Intent getIntent = getIntent();
        String teamid = getIntent.getExtras().getString("teamId");

        final Button newTaskBtn         = (Button) findViewById(R.id.add_task_add_btn);
        final EditText nameET           = (EditText) findViewById(R.id.add_task_name_et);
        final EditText startET          = (EditText) findViewById(R.id.add_task_start_et);
        final EditText endET            = (EditText) findViewById(R.id.add_task_end_et);
        final EditText associationET    = (EditText) findViewById(R.id.add_task_association_et);
        final EditText descriptionET    = (EditText) findViewById(R.id.add_task_description_et);

        listView = (ListView) findViewById(R.id.add_task_members_list);
        adapter = new CurrentMembersAdapter();
        listView.setAdapter(adapter);

        Model.getInstance().getTeamUsers(teamid, new GetTeamUsersCallback() {
            @Override
            public void onResult(List<User> users, Exception e) {
                currentUsers = users;
                adapter.notifyDataSetChanged();
            }
        });

        Model.getInstance().getTeamById(teamid, new GetTeamByIdListener() {
            @Override
            public void onResult(Team team, Exception e) {
                currentTeam = team;
                adapter.notifyDataSetChanged();
                for (TeamMember member : team.getMemberList()) {
                    if (member.getUserId().equals(loggedInUser.getId())) {
                        currentTeamMember = member;
                        break;
                    }
                }
                newTaskBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // input validation
                        if(nameET.getText().toString().isEmpty()){
                            nameET.setError("Please Enter Task Name");
                            return;
                        }
                        if(startET.getText().toString().isEmpty() || !checkDateString(startET.getText().toString())){
                            startET.setError("Invalid Date");
                            return;
                        }
                        if(!endET.getText().toString().isEmpty() && !checkDateString(endET.getText().toString())){
                            endET.setError("Invalid Date");
                            return;
                        }

                        Task newTask = new Task(
                                UUID.randomUUID().toString(),
                                startET.getText().toString(),
                                currentTeamMember,
                                nameET.getText().toString());

                        newTask.setEndDate(endET.getText().toString());
                        newTask.setAssociation(associationET.getText().toString());
                        newTask.setDescription(descriptionET.getText().toString());
                        newTask.setMemberList(taskMembersIds);

                        List<Task> currentTasks = currentTeam.getTaskList();
                        currentTasks.add(newTask);
                        currentTeam.setTaskList(currentTasks);

                        Model.getInstance().editTeam(currentTeam, new EditTeamListener() {
                            @Override
                            public void onResult(Exception e) {
                                Intent resultIntent = new Intent();
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        });

                        Model.getInstance().addTask(newTask, new AddTaskListener() {
                            @Override
                            public void onResult(Exception e) {

                            }
                        });
                    }
                });

            }
        });
    }

    private boolean checkDateString(String dateString) {
        dateString = dateString.replace(".","-");
        dateString = dateString.replace("/","-");
        dateString = dateString.replace("\\","-");
        String [] dateArr = dateString.split("-");
        if(dateArr.length != 3){
            return false;
        }
        dateString = dateArr[2] + "-" + dateArr[1] + "-" + dateArr[0];
        String newDate = dateString.replace('.','-');
        return isValid(newDate);
    }

    private boolean isValid (String text) {
        if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
            return false;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        try {
            df.parse(text);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
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

    class CurrentMembersAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return currentUsers.size();
        }

        @Override
        public Object getItem(int position) {
            return currentUsers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.add_task_members_list_item,null);

//                final CheckBox memberCb1 = (CheckBox) convertView.findViewById(R.id.add_task_members_list_item_checkbox);

            }

            TextView memberName = (TextView) convertView.findViewById(R.id.add_task_members_list_item_name);
            TextView memberEmail = (TextView) convertView.findViewById(R.id.add_task_members_list_item_email);
            CheckBox memberCb2 = (CheckBox) convertView.findViewById(R.id.add_task_members_list_item_checkbox);
            for (TeamMember member : currentTeam.getMemberList()) {
                if (member.getUserId().equals(currentUsers.get(position).getId())) {
                    memberCb2.setChecked(taskMembersIds.contains(member.getId()));
                    break;
                }
            }

            final boolean checked = memberCb2.isChecked();
            memberCb2.setTag(new Integer(position));
            memberCb2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checked) {
                        TeamMember clickedMember;
                        User user = currentUsers.get(position);
                        for (TeamMember member : currentTeam.getMemberList()) {
                            if (member.getUserId().equals(user.getId())) {
                                clickedMember = member;
                                taskMembersIds.remove(clickedMember.getId());
                            }
                        }
                    } else {
                        TeamMember clickedMember;
                        User user = currentUsers.get(position);
                        for (TeamMember member : currentTeam.getMemberList()) {
                            if (member.getUserId().equals(user.getId())) {
                                clickedMember = member;
                                taskMembersIds.add(clickedMember.getId());
                            }
                        }
                    }
                }
            });

            convertView.setTag(position);

            String name = currentUsers.get(position).getName();
            memberName.setText(name);
            String email = currentUsers.get(position).getEmail();
            memberEmail.setText(email);

            return convertView;
        }
    }
}
