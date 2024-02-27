package com.example.taskappty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class teacherDash extends AppCompatActivity {



    ImageView umenu, teacher_task, teacher_appointment, done_task, booked;
    FirebaseAuth fAuth;
    FirebaseUser user;
    TextView get_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dash);


        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        umenu = findViewById(R.id.teacherMenu);
        teacher_task= findViewById(R.id.teacher_task);
        get_user_name = findViewById(R.id.get_user_name);
        teacher_appointment = findViewById(R.id.teacher_appointment);
        done_task = findViewById(R.id.done_task);
        booked = findViewById(R.id.booked);



        //check if user is null or not and append email on the text view
        if (user == null){
            Intent intent = new Intent(teacherDash.this, loginActivity.class);
            startActivity(intent);
            finish();
        }else{
            get_user_name.setText(user.getEmail());

        }

        // menu to direct user to see profile
        umenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), teacherProfile.class);
                startActivity(intent);
                finish();
            }
        });

        // direct user to the student Task page
        teacher_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(teacherDash.this, teacherTask.class);
                startActivity(intent);
            }
        });

        // direct user to the Student Done tasks page
        done_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(teacherDash.this, doneTasks.class);
                startActivity(intent);
            }
        });

        // direct user to the Student Appointment page
        teacher_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(teacherDash.this, teacherAppointment.class);
                startActivity(intent);
            }
        });

        // direct user to the Booked Appointments page
        booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(teacherDash.this, bookedAppointment.class);
                startActivity(intent);
            }
        });

    }

    // Ensure that the dashboard does not get you back to the login activity but exit app
    @Override
    public void finish() {
        super.finish();
        finishAffinity();
    }
}