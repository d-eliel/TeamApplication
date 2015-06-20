package el.team_application.ActivityViews.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import el.team_application.Models.ListItems.TaskMembersListItem;
import el.team_application.R;

/**
 * Created by Eliel on 6/19/2015.
 */
public class TaskMembersListAdapter extends BaseAdapter {

    private Context context;
    private List<TaskMembersListItem> list;

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        TextView nameTV;
        TextView titleTV;
    }

    public TaskMembersListAdapter(Context context, List<TaskMembersListItem> list) {
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
            convertView = inflater.inflate(R.layout.team_tasks_members_list_item,null);

            holder.nameTV    = (TextView) convertView.findViewById(R.id.team_tasks_members_list_item_name);
            holder.titleTV   = (TextView) convertView.findViewById(R.id.team_tasks_members_list_item_title);

            convertView.setTag(holder);
        }else{
            // recycle the already inflated view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTV.setText(list.get(position).getName());
        holder.titleTV.setText(list.get(position).getTitle());

        return convertView;
    }
}