package com.example.taskappty;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AppointmentDetails extends AppCompatActivity {

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_details);


        back = findViewById(R.id.back_arrow);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Retrieve appointment details from the intent
        String teacherName = getIntent().getStringExtra("teacherName");
        String teacherEmail = getIntent().getStringExtra("teacherEmail");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");

        // Display details in TextViews
        TextView teacherNameTextView = findViewById(R.id.teacherNameTextView);
        TextView teacherEmailTextView = findViewById(R.id.teacherEmailTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView timeTextView = findViewById(R.id.timeTextView);

        teacherNameTextView.setText(teacherName);
        teacherEmailTextView.setText(teacherEmail);
        dateTextView.setText(date);
        timeTextView.setText(time);

    }
}