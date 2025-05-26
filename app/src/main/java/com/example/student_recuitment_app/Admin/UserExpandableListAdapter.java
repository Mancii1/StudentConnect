package com.example.student_recuitment_app.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;

import java.util.List;
import java.util.Map;

public class UserExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listGroupTitles; // e.g., "Graduates", "Employers"
    private Map<String, List<User>> listData; // Mapping of group titles to user lists

    private DatabaseHelper dbHelper;

    public UserExpandableListAdapter(Context context, List<String> listGroupTitles, Map<String, List<User>> listData) {
        this.context = context;
        this.listGroupTitles = listGroupTitles;
        this.listData = listData;
    }

    @Override
    public int getGroupCount() {
        return listGroupTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listData.get(listGroupTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroupTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(listGroupTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    // Group header view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }
        TextView groupTextView = (TextView) convertView.findViewById(android.R.id.text1);
        groupTextView.setText(groupTitle);
        return convertView;
    }

    // Child item view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        User user = (User) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.user_list_item, parent, false);
        }
        TextView nameTextView = convertView.findViewById(R.id.userNameTextView);
        TextView emailTextView = convertView.findViewById(R.id.userEmailTextView);
        TextView detailsTextView = convertView.findViewById(R.id.userDetailsTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteUserButton);

        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        detailsTextView.setText(user.getDetails());

        deleteButton.setOnClickListener(v -> {
            // Remove from database
            dbHelper.deleteUser(user.getEmail()); // Implement this method in your DatabaseHelper

            // Remove from list and update adapter
            listData.get(listGroupTitles.get(groupPosition)).remove(childPosition);
            notifyDataSetChanged();
            Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
