package el.team_application.ActivityViews.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import el.team_application.Models.ListItems.TeamMembersListItem;
import el.team_application.R;

/**
 * Created by Eliel on 6/12/2015.
 */
public class MembersListAdapter extends BaseAdapter {

    private Context context;
    private List<TeamMembersListItem> list;

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        TextView nameTV;
    }

    public MembersListAdapter(Context context, List<TeamMembersListItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.team_members_list_item,null);

            holder.nameTV = (TextView) convertView.findViewById(R.id.team_member_item_name);
            convertView.setTag(holder);
        }else{
            // recycle the already inflated view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTV.setText(list.get(position).getName());

        return convertView;
    }
}
