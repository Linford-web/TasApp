package com.example.taskappty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.Adapter.bookedAppointmentAdapter;
import com.example.taskappty.model.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class bookedAppointment extends AppCompatActivity {

    ImageView back;

    RecyclerView recyclerView;
    bookedAppointmentAdapter adapter;
    String currentUserId;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_appointment);

        back = findViewById(R.id.back_arrow);
        recyclerView = findViewById(R.id.bookedRv);
        currentUserId = FirebaseAuth.getInstance().getUid();

        fStore = FirebaseFirestore.getInstance();

        // deal with the back functionality on the top bar nav
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up RecyclerView and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new bookedAppointmentAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Set item click listener for the adapter
        adapter.setOnItemClickListener(new bookedAppointmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Appointment appointment) {
                // Handle item click, start new activity with appointment details
                Intent intent = new Intent(bookedAppointment.this, AppointmentDetails.class);
                intent.putExtra("teacherName", appointment.getTeacherName());
                intent.putExtra("teacherEmail", appointment.getTeacherEmail());
                intent.putExtra("date", appointment.getAppointmentDate());
                intent.putExtra("time", appointment.getSelectedTimeSlot());
                startActivity(intent);
            }
        });

        // Fetch booked appointments from Firestore
        fetchBookedAppointments();

    }

    private void fetchBookedAppointments() {
        fStore.collection("bookedAppointments")
                .whereEqualTo("creatorId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Appointment> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            appointments.add(appointment);
                        }
                        // Fetch appointments where the current user is the confirming user
                        fetchConfirmingAppointments(appointments);
                    } else {
                        Log.e("bookedAppointment", "Error getting booked appointments", task.getException());
                    }
                });
    }

    private void fetchConfirmingAppointments(List<Appointment> creatorAppointments) {
        fStore.collection("bookedAppointments")
                .whereEqualTo("confirmingUserId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            // Add only if not already added (to avoid duplicates)
                            if (!creatorAppointments.contains(appointment)) {
                                creatorAppointments.add(appointment);
                            }
                        }
                        updateRecyclerView(creatorAppointments);
                    } else {
                        Log.e("bookedAppointment", "Error getting confirming appointments", task.getException());
                    }
                });
    }

    private void updateRecyclerView(List<Appointment> appointments) {
        adapter.setData(appointments);
        adapter.notifyDataSetChanged();
    }

}