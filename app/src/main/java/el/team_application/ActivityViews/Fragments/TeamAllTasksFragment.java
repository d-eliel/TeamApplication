package el.team_application.ActivityViews.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import el.team_application.ActivityViews.Adapters.TasksListAdapter;
import el.team_application.ActivityViews.Adapters.TaskMembersListAdapter;
import el.team_application.Listeners.Teams.GetTeamByIdListener;
import el.team_application.Listeners.User.GetTeamUsersCallback;
import el.team_application.Models.Entities.Task;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;
import el.team_application.Models.ListItems.TaskMembersListItem;
import el.team_application.Models.Model;
import el.team_application.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamAllTasksFragment extends Fragment {
    private boolean _gotUsers                           = false;
    private boolean _gotMembers                         = false;
    private boolean collapseList                        = false;
    private List<User> _usersList                       = new LinkedList<>();
    private List<TeamMember> _membersList               = new LinkedList<>();
    private List<Task> _dataList                        = new LinkedList<>();
    private List<TaskMembersListItem> _currTaskMembers  = new LinkedList<>();

    private LinearLayout tasksListLayout;
    private ListView taskMembersListView;
    private ListView tasksListView;
    private TasksListAdapter tasksListAdapter;
    private TaskMembersListAdapter taskMembersListAdapter;

    public TeamAllTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team_tasks,container,false);

        /* empty list message */
//        final TextView emptyListMessage = (TextView) v.findViewById(R.id.team)

        /* Selected task view details */
        final TextView selectedName = (TextView) v.findViewById(R.id.team_tasks_task_name);
        final TextView selectedStatus = (TextView) v.findViewById(R.id.team_tasks_task_status);
        final TextView selectedStart = (TextView) v.findViewById(R.id.team_tasks_task_start);
        final TextView selectedEnd = (TextView) v.findViewById(R.id.team_tasks_task_end);
        final TextView selectedAssociation = (TextView) v.findViewById(R.id.team_tasks_task_association);
        final TextView selectedDescription = (TextView) v.findViewById(R.id.team_tasks_task_description);
        final TextView selectedNumOfMembers = (TextView) v.findViewById(R.id.team_tasks_task_total_members);

        tasksListLayout = (LinearLayout) v.findViewById(R.id.team_tasks_list_layout);

        // get team id argument
        String teamid = getArguments().getString("teamId");
        tasksListView = (ListView) v.findViewById(R.id.team_tasks_list);
        taskMembersListView = (ListView) v.findViewById(R.id.team_tasks_task_members_list);

        final TextView emptyText = (TextView) v.findViewById(R.id.team_tasks_empty_message);
        tasksListView.setEmptyView(emptyText);

        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedName.setText("Task Name: \t"+_dataList.get(position).getName());
                selectedStatus.setText("Status: \t"+_dataList.get(position).getStatus().toString());
                selectedStart.setText("Start Date: \t"+_dataList.get(position).getStartDate());
                selectedNumOfMembers.setText("Total Members: \t"+_dataList.get(position).getMemberList().size());

                // set task members list
                _currTaskMembers = new LinkedList<TaskMembersListItem>();
                for(String memberId : _dataList.get(position).getMemberList()){
                    for(TeamMember member :_membersList){
                        if(member.getId().equals(memberId)){
                            for(User user : _usersList){
                                if(user.getId().equals(member.getUserId())){
                                    _currTaskMembers.add(new TaskMembersListItem(member.getId(),user.getName(), member.getJobTitle()));
                                }
                            }
                        }
                    }
                }
                taskMembersListAdapter = new TaskMembersListAdapter(getActivity(), _currTaskMembers);
                taskMembersListView.setAdapter(taskMembersListAdapter);

                if(_dataList.get(position).getEndDate().isEmpty()){
                    selectedEnd.setVisibility(View.GONE);
                }else{
                    selectedEnd.setVisibility(View.VISIBLE);
                    selectedEnd.setText("End Date: \t"+_dataList.get(position).getEndDate());
                }

                if(_dataList.get(position).getAssociation().isEmpty()){
                    selectedAssociation.setVisibility(View.GONE);
                }else{
                    selectedAssociation.setVisibility(View.VISIBLE);
                    selectedAssociation.setText("Association: \t"+_dataList.get(position).getAssociation());
                }

                if(_dataList.get(position).getDescription().isEmpty()){
                    selectedDescription.setVisibility(View.GONE);
                }else{
                    selectedDescription.setVisibility(View.VISIBLE);
                    selectedDescription.setText("Description: \t"+_dataList.get(position).getDescription());
                }
            }
        });

        // set list data
        Model.getInstance().getTeamById(teamid, new GetTeamByIdListener() {
            @Override
            public void onResult(Team team, Exception e) {
                _membersList = team.getMemberList();
                _dataList = team.getTaskList();
                _gotMembers = true;
                if(_gotMembers && _gotUsers) {
                    tasksListAdapter = new TasksListAdapter(getActivity(), _dataList);
                    tasksListView.setAdapter(tasksListAdapter);
                    taskMembersListAdapter = new TaskMembersListAdapter(getActivity(), _currTaskMembers);
                    taskMembersListView.setAdapter(taskMembersListAdapter);
                }
            }
        });

        Model.getInstance().getTeamUsers(teamid, new GetTeamUsersCallback() {
            @Override
            public void onResult(List<User> users, Exception e) {
                _usersList = users;
                _gotUsers = true;
                if(_gotUsers && _gotMembers){
                    tasksListAdapter = new TasksListAdapter(getActivity(), _dataList);
                    tasksListView.setAdapter(tasksListAdapter);
                    taskMembersListAdapter = new TaskMembersListAdapter(getActivity(), _currTaskMembers);
                    taskMembersListView.setAdapter(taskMembersListAdapter);
                }
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_team_tasks,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.menu_team_tasks_collapse_list){
            if(!collapseList){
                collapseList = !collapseList;
                tasksListLayout.setVisibility(View.GONE);
                taskMembersListView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;

            }else{
                collapseList = !collapseList;
                tasksListLayout.setVisibility(View.VISIBLE);
                taskMembersListView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
