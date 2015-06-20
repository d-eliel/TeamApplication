package el.team_application.ActivityViews.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import el.team_application.ActivityViews.Fragments.TeamAllTasksFragment;
import el.team_application.ActivityViews.Fragments.TeamManageFragment;
import el.team_application.ActivityViews.Fragments.TeamMembersFragment;
import el.team_application.ActivityViews.Fragments.TeamMyTasksFragment;
import el.team_application.R;

/**
 * Created by Eliel on 6/16/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    String teamId;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabs, String mTeamId) {
        super(fm);

        this.Titles     = mTitles;
        this.NumbOfTabs = mNumbOfTabs;
        this.teamId     = mTeamId;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 3)
        {
            TeamManageFragment teamManageFragment = new TeamManageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("teamId", this.teamId);
            teamManageFragment.setArguments(bundle);
            return teamManageFragment;
        }
        else if(position == 2)
        {
            TeamMyTasksFragment teamMyTasksFragment = new TeamMyTasksFragment();
            Bundle bundle = new Bundle();
            bundle.putString("teamId", this.teamId);
            teamMyTasksFragment.setArguments(bundle);
            return teamMyTasksFragment;
        }
        else if(position == 1)
        {
            TeamAllTasksFragment teamAllTasksFragment = new TeamAllTasksFragment();
            Bundle bundle = new Bundle();
            bundle.putString("teamId", this.teamId);
            teamAllTasksFragment.setArguments(bundle);
            return teamAllTasksFragment;
        }
        else // Go to tab 0
        {
            TeamMembersFragment teamMembersFragment = new TeamMembersFragment();
            Bundle bundle = new Bundle();
            bundle.putString("teamId", this.teamId);
            teamMembersFragment.setArguments(bundle);
            return teamMembersFragment;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
