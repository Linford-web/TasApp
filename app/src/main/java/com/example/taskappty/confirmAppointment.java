package com.example.taskappty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class confirmAppointment extends AppCompatActivity {

    TextView teacherNameTextView, teacherEmailTextView, dateTextView, timeSlotTextView, userIdTextView;
    ProgressBar progressBar;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_appointment);

        teacherNameTextView = findViewById(R.id.teacher_name);
        teacherEmailTextView = findViewById(R.id.teacher_email);
        dateTextView = findViewById(R.id.appointment_date);
        timeSlotTextView = findViewById(R.id.appointment_time);
        userIdTextView = findViewById(R.id.creator_id);
        progressBar = findViewById(R.id.progressbar);
        backButton = findViewById(R.id.back_arrow);


        // Handle the back button click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Retrieve data from the intent
        Intent intent = getIntent();
        String teacherName = intent.getStringExtra("teacherName");
        String teacherEmail = intent.getStringExtra("teacherEmail");
        String appointmentDate = intent.getStringExtra("appointmentDate");
        String selectedTimeSlot = intent.getStringExtra("selectedTimeSlot");
        String userId = intent.getStringExtra("userId");

        // Display the data in TextViews
        teacherNameTextView.setText(teacherName);
        teacherEmailTextView.setText(teacherEmail);
        dateTextView.setText(appointmentDate);
        timeSlotTextView.setText(selectedTimeSlot);
        userIdTextView.setText(userId);


        // Handle the confirm button click
        Button confirmButton = findViewById(R.id.btnBook);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmAppointments();

            }
        });
    }

    private void confirmAppointments() {
        progressBar.setVisibility(View.VISIBLE);

        // Get the user ID who clicks the confirm button
        String confirmingUserId = FirebaseAuth.getInstance().getUid();

        // Add your logic to store the data in the "bookedAppointments" collection
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        // Create a Map to represent the appointment details
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("confirmingUserId", confirmingUserId);
        appointmentData.put("teacherName", teacherNameTextView.getText().toString().trim());
        appointmentData.put("teacherEmail", teacherEmailTextView.getText().toString().trim());
        appointmentData.put("appointmentDate", dateTextView.getText().toString().trim());
        appointmentData.put("selectedTimeSlot", timeSlotTextView.getText().toString().trim());
        appointmentData.put("creatorId", userIdTextView.getText().toString().trim());

        // Add a new document to the "bookedAppointments" collection
        fStore.collection("bookedAppointments")
                .add(appointmentData)
                .addOnSuccessListener(documentReference -> {

                    progressBar.setVisibility(View.GONE);

                    // Optionally, navigate to the BookedAppointmentsActivity
                    startActivity(new Intent(confirmAppointment.this, bookedAppointment.class));

                    // Document added successfully
                    Toast.makeText(confirmAppointment.this, "Appointment Confirmed And Booked Successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Optionally, close the activity after confirming
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        // Error occurred while adding the document
                        Toast.makeText(confirmAppointment.this, "Failed to Book Appointment", Toast.LENGTH_SHORT).show();
                    }

                });

    }
}