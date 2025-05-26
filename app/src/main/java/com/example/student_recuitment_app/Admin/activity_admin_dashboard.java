package com.example.student_recuitment_app.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.student_recuitment_app.Admin.User;
import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;
import com.example.student_recuitment_app.authantication.loginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.Button;

public class activity_admin_dashboard extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private UserExpandableListAdapter adapter;
    private List<String> listGroupTitles;
    private Map<String, List<User>> listData;
    private DatabaseHelper dbHelper;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        expandableListView = findViewById(R.id.expandableListView);
        button3 = findViewById(R.id.button3);
        dbHelper = new DatabaseHelper(this);

        // Fetch users from the database
        List<User> graduates = dbHelper.getALGraduates();
        List<User> employers = dbHelper.getAllEmployers();

        listGroupTitles = new ArrayList<>();
        listGroupTitles.add("Graduates");
        listGroupTitles.add("Employers");

        listData = new HashMap<>();
        listData.put("Graduates", graduates);
        listData.put("Employers", employers);

        adapter = new UserExpandableListAdapter(this, listGroupTitles, listData);
        expandableListView.setAdapter(adapter);

        button3.setOnClickListener(v -> {
            startActivity(new Intent(this, loginActivity.class));
            finish();
        });
    }
}
