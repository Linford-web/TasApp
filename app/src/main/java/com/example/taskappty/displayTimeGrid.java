package com.example.taskappty;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.Adapter.TimeSlotAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class displayTimeGrid extends AppCompatActivity {

    TextView AppointmentDate;
    RecyclerView recyclerView;

    ImageView back;
    FirebaseFirestore fStore;
    String timeSlot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_time_grid);


        AppointmentDate = findViewById(R.id.tvAppointmentDate);
        recyclerView = findViewById(R.id.timeSlotRecyclerView);
        back = findViewById(R.id.back_arrow);

        // back arrow implementation
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get data from intent
        Intent intent = getIntent();
        String appointmentDate = intent.getStringExtra("appointmentDate");
        String startTime = intent.getStringExtra("startTime");
        String endTime = intent.getStringExtra("endTime");
        String teacherName = intent.getStringExtra("teacherName");
        String teacherEmail = intent.getStringExtra("teacherEmail");
        String userId = intent.getStringExtra("userId");

        // Set appointment date
        AppointmentDate.setText(appointmentDate);

        // Initialize FirebaseFireStore
        fStore = FirebaseFirestore.getInstance();

        // Generate time slots
        List<String> timeSlots = generateTimeSlots(startTime, endTime);

        // Make the recyclerview to show the item list in a grid layout with 3 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        // Set up grid view
        TimeSlotAdapter adapter = new TimeSlotAdapter(this, timeSlots);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        // when the time slot is clicked it will show a toast message
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int position = rv.getChildAdapterPosition(child);
                    timeSlot = timeSlots.get(position);
                    Toast.makeText(displayTimeGrid.this, "Selected time: " + timeSlot, Toast.LENGTH_SHORT).show();
                    // Create an intent to start ConfirmAppointmentActivity
                    Intent intent = new Intent(displayTimeGrid.this, confirmAppointment.class);

                    // Pass the selected time slot as an extra to the ConfirmAppointmentActivity
                    intent.putExtra("selectedTimeSlot", timeSlot);

                    // Add any additional data you want to pass to ConfirmAppointmentActivity
                    intent.putExtra("teacherName", teacherName);
                    intent.putExtra("teacherEmail", teacherEmail);
                    intent.putExtra("appointmentDate", appointmentDate);
                    intent.putExtra("userId", userId);

                    // Start ConfirmAppointmentActivity
                    startActivity(intent);

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }


    private List<String> generateTimeSlots(String startTime, String endTime) {
        List<String> timeSlots = new ArrayList<>();

        // Parse start and end times
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            // Generate time slots
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            while (calendar.getTime().before(endDate)) {
                timeSlots.add(sdf.format(calendar.getTime()));
                calendar.add(Calendar.MINUTE, 15);
            }
        } catch (Exception e) {
            // If parsing fails, use the current format of the input strings
            sdf = new SimpleDateFormat("H : m", Locale.getDefault());
            try {
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);

                // Generate time slots
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                while (calendar.getTime().before(endDate)) {
                    timeSlots.add(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.MINUTE, 15);
                }

            } catch (Exception ex) {
                e.printStackTrace();
            }

        }

        return timeSlots;
    }

}
