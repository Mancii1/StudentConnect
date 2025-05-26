package com.example.student_recuitment_app.Graduate.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.student_recuitment_app.R;

import java.util.List;

public class GraduateAdapter extends ArrayAdapter<Graduate> {
    private Context context;
    private List<Graduate> graduates;

    public GraduateAdapter(Context context, List<Graduate> graduates) {
        super(context, 0, graduates);
        this.context = context;
        this.graduates = graduates;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Graduate graduate = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.graduate_list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        TextView tvDegree = convertView.findViewById(R.id.tvDegree);
        TextView tvLocation = convertView.findViewById(R.id.tvLocation);
        TextView tvSkills = convertView.findViewById(R.id.tvSkills);
        TextView tvBio = convertView.findViewById(R.id.tvBio);

        tvName.setText(graduate.getName());
        tvEmail.setText("Email: " + graduate.getEmail());
        tvDegree.setText("Degree: " + graduate.getGrad_degree());
        tvLocation.setText("Location: " + graduate.getGrad_loc());
        tvSkills.setText("Skills: " + graduate.getGrad_skills());
        tvBio.setText("Bio: " + graduate.getGrad_bio());

        return convertView;
    }
}

