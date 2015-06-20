package el.team_application.ActivityViews.Fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import el.team_application.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamManageFragment extends Fragment {


    public TeamManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_team_manage,container,false);
        return v;
    }


}
