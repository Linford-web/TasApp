package com.example.taskappty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskappty.model.AppointmentModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class confirmAppointment extends AppCompatActivity {

    Button btnBook;
    TextView tvName, tvEmail, tvDate, tvTime;
    ImageView back;
    ProgressBar progressBar;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_appointment);


        tvName = findViewById(R.id.teacher_name);
        tvEmail = findViewById(R.id.teacher_email);
        tvDate = findViewById(R.id.appointment_date);
        tvTime = findViewById(R.id.appointment_time);
        btnBook = findViewById(R.id.btnBook);
        back = findViewById(R.id.back_arrow);
        progressBar = findViewById(R.id.progressbar);
        fStore = FirebaseFirestore.getInstance();

        // back arrow logic
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get the data from the intent extras
        Intent intent = getIntent();
        String teacherName = intent.getStringExtra("teacherName");
        String teacherEmail = intent.getStringExtra("teacherEmail");
        String appointmentDate = intent.getStringExtra("appointmentDate");
        String selectedTimeSlot = intent.getStringExtra("selectedTimeSlot");

        // Check if the data is present in the intent
        if (teacherName == null || teacherEmail == null || appointmentDate == null) {
            // Handle the case where data is missing
            Toast.makeText(this, "Cannot Display Appointment Details", Toast.LENGTH_SHORT).show();
            return;

        }
        // Set the data to the text views
        tvName.setText(teacherName);
        tvEmail.setText(teacherEmail);
        tvDate.setText(appointmentDate);
        tvTime.setText(selectedTimeSlot);

        // Set up book button
       btnBook.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               progressBar.setVisibility(View.VISIBLE);

               String teacherName = tvName.getText().toString().trim();
               String teacherEmail = tvEmail.getText().toString().trim();
               String dayOfWeek = tvDate.getText().toString().trim();
               String selectedTimeSlot = tvTime.getText().toString().trim();


                   Toast.makeText(confirmAppointment.this, teacherName, Toast.LENGTH_SHORT).show();

                   //save task details to FireStore.
                   AppointmentModel appointmentModel = new AppointmentModel(teacherName, teacherEmail, dayOfWeek, FirebaseFirestore.getInstance().toString(), selectedTimeSlot);

                   fStore.collection("bookedAppointments")
                           .add(appointmentModel)
                           .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                               @Override
                               public void onSuccess(DocumentReference documentReference) {
                                   Log.d("TAG", "Document added with ID: " + documentReference.getId());

                                   // Ensures that the success_layout disappears after 3 seconds
                                   new Handler().postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           Intent intent = new Intent(confirmAppointment.this, bookedAppointment.class);
                                           startActivity(intent);
                                           finish();
                                       }
                                   }, 1000);

                                   //make success layout appear after Appointment is successfully added
                                   findViewById(R.id.successLayout).setVisibility(View.VISIBLE);
                                   // make add task layout to disappear
                                   findViewById(R.id.appointment_details).setVisibility(View.GONE);
                                   //make top navigation disappear
                                   findViewById(R.id.top_bar_nav).setVisibility(View.GONE);

                                   progressBar.setVisibility(View.GONE);

                               }
                           })
                           .addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Log.d("LOG", "Error Adding Document", e);
                                   progressBar.setVisibility(View.GONE);

                               }
                           });

           }
       });

    }
}