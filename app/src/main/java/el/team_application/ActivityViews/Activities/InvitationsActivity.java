package el.team_application.ActivityViews.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import el.team_application.Listeners.Invitations.AcceptInviteListener;
import el.team_application.Listeners.Invitations.DeclineInviteListener;
import el.team_application.Listeners.Invitations.GetInvitationsForUserListener;
import el.team_application.Models.Entities.Invitation;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;
import el.team_application.Models.Model;
import el.team_application.R;

public class InvitationsActivity extends ActionBarActivity {
    User loggedUser;
    ListView listView;
    InvitationsAdapter adapter;
    List<Invitation> invitations = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);

        loggedUser = Model.getInstance().getLoggedInUser();
        listView = (ListView) findViewById(R.id.invitations_list);
        adapter  = new InvitationsAdapter();
        listView.setAdapter(adapter);

        final TextView emptyText = (TextView) findViewById(R.id.invitations_empty_message);
        listView.setEmptyView(emptyText);

        Model.getInstance().getInvitationsForUser(loggedUser.getId(), new GetInvitationsForUserListener() {
            @Override
            public void onResult(List<Invitation> list, Exception e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
                invitations = list;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invitations, menu);
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
                Toast.makeText(getApplicationContext(), "Exit   ", Toast.LENGTH_LONG).show();
                finish();
                System.exit(1);
        }

        return super.onOptionsItemSelected(item);
    }

    class InvitationsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return invitations.size();
        }

        @Override
        public Object getItem(int position) {
            return invitations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.invitations_list_item,null);
            }

            TextView userEmail = (TextView) convertView.findViewById(R.id.invitations_inviter_email_tv);
            TextView teamName = (TextView) convertView.findViewById(R.id.invitations_inviter_team_tv);

            Button acceptBtn = (Button) convertView.findViewById(R.id.invitations_accept_btn);
            Button declineBtn = (Button) convertView.findViewById(R.id.invitations_decline_btn);

            acceptBtn.setTag(new Integer(position));
            declineBtn.setTag(new Integer(position));

            final View finalConvertView = convertView;
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Model.getInstance().acceptInvite(invitations.get(position), new AcceptInviteListener() {
                        @Override
                        public void onResult(Exception e) {
                            if (e != null) {
                                // TODO: if no internet (Disconnected)
                            }
                            adapter.notifyDataSetChanged();
                            finalConvertView.refreshDrawableState();
                            parent.refreshDrawableState();
                        }
                    });
                }
            });

            declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Model.getInstance().declineInvite(invitations.get(position), new DeclineInviteListener() {
                        @Override
                        public void onResult(Exception e) {
                            if (e != null) {
                                // TODO: if no internet (Disconnected)
                            }
                            adapter.notifyDataSetChanged();
                            finalConvertView.refreshDrawableState();
                            parent.refreshDrawableState();
                        }
                    });
                }
            });

            convertView.setTag(position);

            userEmail.setText(invitations.get(position).getInviterEmail());
            teamName.setText(invitations.get(position).getTeamName());

            return convertView;
        }
    }

}
