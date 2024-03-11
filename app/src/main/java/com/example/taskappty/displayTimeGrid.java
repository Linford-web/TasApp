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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.Adapter.TimeSlotAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class displayTimeGrid extends AppCompatActivity {

    TextView AppointmentDate;
    RecyclerView recyclerView;

    ImageView back;
    FirebaseFirestore fStore;
    String timeSlot;
    List<String> timeSlots = new ArrayList<>();


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
                        // Update the UI for the selected time slot
                        updateTimeSlotUI(child);
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

                    }
                    return true;
                }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    private void updateTimeSlotUI(View child) {

        // Add your logic here to change the color and text of the selected time slot
        // TextView in the layout, you can update its text
        TextView textView = child.findViewById(R.id.txt_appointmentStatus);
        if (textView != null) {
            // Set the text of the TextView to "Booked"
            textView.setText("Booked");
        }
        // You may also want to disable further clicks on the time slot
        Objects.requireNonNull(textView).setEnabled(false);

        // change the background color of the time slot
        child.setBackgroundColor(ContextCompat.getColor(displayTimeGrid.this, R.color.orange));
        // disable the view to prevent further clicks
        child.setEnabled(false);
    }

    private boolean isTimeSlotBooked(int position) {

        // Get the list of booked time slots from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference bookedAppointmentsCollection = db.collection("bookedAppointments");

        // Get the time slot at the given position
        String selectedTimeSlot = timeSlots.get(position);

        // Query the bookedAppointmentsCollection to check if the selected time slot is booked
        Query query = bookedAppointmentsCollection.whereEqualTo("selectedTimeSlot", selectedTimeSlot);

        // Perform the query asynchronously
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Check if the query returned any results
                    boolean isBooked = !task.getResult().isEmpty();

                    // Now you can handle the result as needed
                    handleBookingResult(isBooked);
                } else {
                    // Handle the error if the query fails
                    handleBookingError(task.getException());
                }
            }
        });
        return false;
    }


    // Handle the booking result (e.g., update UI)
    private void handleBookingResult(boolean isBooked) {
        if (isBooked) {
            // The time slot is booked
            // Implement the logic to notify the user or take necessary action
            // You might disable the time slot or show a message to the user
        } else {
            // The time slot is not booked
            // Continue allowing the user to select the time slot
            // Implement the logic as needed
        }
    }

    // Handle the booking error (e.g., log the error)
    private void handleBookingError(Exception exception) {
        exception.printStackTrace();
        // Handle the error as needed (e.g., log, show error message)

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
