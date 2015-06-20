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

import el.team_application.ActivityViews.Adapters.MembersListAdapter;
import el.team_application.Listeners.Teams.GetTeamByIdListener;
import el.team_application.Listeners.User.GetTeamUsersCallback;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.ListItems.TeamMembersListItem;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamMembersFragment extends Fragment {
    private boolean _gotUsers                   = false;
    private boolean _gotMembers                 = false;
    private boolean collapseList                = false;
    private List<User> _usersList               = new LinkedList<>();
    private List<TeamMember> _membersList       = new LinkedList<>();
    private List<TeamMembersListItem> _dataList = new LinkedList<>();

    private LinearLayout membersListLayout;
    private ListView membersListView;
    private MembersListAdapter membersListAdapter;

    public TeamMembersFragment() {
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
        View v = inflater.inflate(R.layout.fragment_team_members, container, false);

        /* Selected task view details */
        final TextView selectedName = (TextView) v.findViewById(R.id.team_members_member_name);
        final TextView selectedTitle = (TextView) v.findViewById(R.id.team_members_member_title);
        final TextView selectedEmail = (TextView) v.findViewById(R.id.team_members_member_email);
        final TextView selectedPhone = (TextView) v.findViewById(R.id.team_members_member_phone);
        final TextView selectedRole = (TextView) v.findViewById(R.id.team_members_member_role);

        // get team id argument
        String teamid = getArguments().getString("teamId");
        membersListLayout = (LinearLayout) v.findViewById(R.id.team_members_list_layout);
        membersListView = (ListView) v.findViewById(R.id.team_members_list);

        membersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedName.setText("Member Name: \t\t"+_dataList.get(position).getName());
                selectedTitle.setText("Title: \t\t"+_dataList.get(position).getJobTitle());
                selectedEmail.setText("Email: \t\t"+_dataList.get(position).getEmail());
                selectedPhone.setText("Phone Number: \t\t"+_dataList.get(position).getPhone());
                selectedRole.setText(_dataList.get(position).getRole().toString());
            }
        });

        // set list data
        Model.getInstance().getTeamById(teamid, new GetTeamByIdListener() {
            @Override
            public void onResult(Team team, Exception e) {
                _membersList = team.getMemberList();
                _gotMembers = true;
                if(_gotMembers && _gotUsers) {
                    setTeamMembersListItems();
                    membersListAdapter = new MembersListAdapter(getActivity(), _dataList);
                    membersListView.setAdapter(membersListAdapter);
//                  membersListAdapter.notifyDataSetChanged();
                }
            }
        });

        Model.getInstance().getTeamUsers(teamid, new GetTeamUsersCallback() {
            @Override
            public void onResult(List<User> users, Exception e) {
                _usersList = users;
                _gotUsers = true;
                if(_gotUsers && _gotMembers){
                    setTeamMembersListItems();
                    membersListAdapter = new MembersListAdapter(getActivity(), _dataList);
                    membersListView.setAdapter(membersListAdapter);
//                  membersListAdapter.notifyDataSetChanged();
                }
            }
        });

        return v;
    }

    private void setTeamMembersListItems() {
        for(int i=0; i<_usersList.size(); i++){
            User currUser = _usersList.get(i);
            for(int j=0; j<_membersList.size(); j++){
                TeamMember currMember = _membersList.get(j);
                if(currUser.getId().equals(currMember.getUserId())){
                    _dataList.add(new TeamMembersListItem(currUser, currMember));
                }
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_team_members,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.menu_team_members_collapse_list){
            if(!collapseList){
                collapseList = !collapseList;
                membersListLayout.setVisibility(View.GONE);
            }else{
                collapseList = !collapseList;
                membersListLayout.setVisibility(View.VISIBLE);
            }
        }

        return super.onOptionsItemSelected(item);
    }


}
