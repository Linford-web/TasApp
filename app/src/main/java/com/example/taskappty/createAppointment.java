package com.example.taskappty;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.taskappty.model.AppointmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class createAppointment extends AppCompatActivity {

    EditText editTextName, editTextEmail;
    AppointmentManager appointmentManager;
    ProgressBar progressBar;
    AppointmentAdapter appointmentAdapter;

    Button createButton, addTime, addDate;
    TextView datePicker, timePicker;
    String startTime, endTime;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);


        fStore = FirebaseFirestore.getInstance();
        //fAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        progressBar = findViewById(R.id.progressbar);

        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.startTimePicker);

        addTime = findViewById(R.id.startTimePickerBtn);
        addDate = findViewById(R.id.datePickerBtn);
        createButton = findViewById(R.id.createAppointmentBtn);

        appointmentAdapter = new AppointmentAdapter(new ArrayList<>());
        appointmentManager = new AppointmentManager();


        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });
        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                createAppointment();
            }
        });
    }

    private void openDateDialog() {
        // Use a single TimePickerDialog to allow the user to select both start and end times
        TimePickerDialog timeSpanDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int startHourOfDay, int startMinute) {
                startTime = String.valueOf(startHourOfDay) + " : " + String.valueOf(startMinute);

                // Show another TimePickerDialog to select the end time
                TimePickerDialog endTimeDialog = new TimePickerDialog(createAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int endHourOfDay, int endMinute) {
                        endTime = String.valueOf(endHourOfDay) + " : " + String.valueOf(endMinute);

                        // Display the selected time span or update UI as needed
                        timePicker.setText(String.format("%s to %s", startTime, endTime));
                    }
                }, startHourOfDay, startMinute, true);
                endTimeDialog.show();
            }
        }, 12, 0, true);
        timeSpanDialog.show();
    }


    private void openDialog() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePicker.setText(String.format("%s : %s : %s", String.valueOf(year), String.valueOf(month), String.valueOf(dayOfMonth)));
            }
        },2024,4,10);

        dialog.show();
    }

    private void createAppointment() {

        String teacherName = editTextName.getText().toString().trim();
        String teacherEmail = editTextEmail.getText().toString().trim();
        String dayOfWeek = datePicker.getText().toString();


        // Ensure that both start time and end time are selected
        if (startTime == null || endTime == null) {
            Toast.makeText(this, "Please select both start time and end time", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        // Construct the time span by combining start and end times
        String timeSpan = startTime + " to " + endTime;
        Toast.makeText(createAppointment.this, "Added Successfully", Toast.LENGTH_SHORT).show();

        CollectionReference appointmentsCollection = fStore.collection("Appointments");

        fAuth = FirebaseAuth.getInstance();
        String  userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        AppointmentModel appointmentModel = new AppointmentModel(teacherName, dayOfWeek, teacherEmail, startTime, endTime);

        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("teacherName", appointmentModel.getTeacherName());
        appointmentData.put("dayOfWeek", appointmentModel.getDayOfWeek());
        appointmentData.put("teacherEmail", appointmentModel.getTeacherEmail());
        appointmentData.put("startTime", appointmentModel.getStartTime());
        appointmentData.put("endTime", appointmentModel.getEndTime());

        // Include userId in the appointment data
        appointmentData.put("userId", userId);

        appointmentsCollection.add(appointmentData)
                .addOnSuccessListener(documentReference -> {
                    String appointmentId = documentReference.getId();
                    appointmentModel.setAppointmentId(appointmentId);
                    Toast.makeText(createAppointment.this, "Appointment Time Added Successfully", Toast.LENGTH_SHORT).show();
                    //Notify The Adapter when about the new data
                    appointmentAdapter.addAppointment(appointmentModel);

                    Intent intent = new Intent(createAppointment.this, teacherAppointment.class);
                    intent.putExtra("appointmentModel", appointmentModel);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(createAppointment.this, "Error Adding Appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });

    }
}