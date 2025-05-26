package com.example.student_recuitment_app.Employer;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.Graduate.Profile.Graduate;
import com.example.student_recuitment_app.Graduate.Profile.GraduateAdapter;
import com.example.student_recuitment_app.R;

import java.util.List;

public class ViewGraduatesActivity extends AppCompatActivity {

    ListView listViewGrad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_graduates);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listViewGrad = findViewById(R.id.listViewGrad);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Graduate> graduates = dbHelper.getAllGraduates();

        GraduateAdapter adapter = new GraduateAdapter(this, graduates);
        listViewGrad.setAdapter(adapter);

    }
}