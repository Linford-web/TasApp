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
        adapter = new bookedAppointmentAdapter(new ArrayList<>(), true);
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
                .whereEqualTo("confirmingUserId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Appointment> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                                appointments.add(appointment);
                        }
                        // Fetch appointments where the creatorId matches the current user's ID
                        fetchAppointmentsByCreatorId(currentUserId, appointments);
                    } else {
                        Log.e("bookedAppointment", "Error getting booked appointments", task.getException());
                    }
                });
    }

    private void fetchAppointmentsByCreatorId(String currentUserId, List<Appointment> appointments) {
        fStore.collection("bookedAppointments")
                .whereEqualTo("creatorId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            // Add appointment if not already present (to avoid duplicates)
                            if (!appointments.contains(appointment)) {
                                appointments.add(appointment);
                            }
                        }
                        // Update the UI with the fetched appointments
                        updateRecyclerView(appointments);
                    } else {
                        Log.e("bookedAppointment", "Error getting appointments by creatorId", task.getException());
                    }
                });

    }

    private void updateRecyclerView(List<Appointment> appointments) {
        adapter.setData(appointments);
        adapter.notifyDataSetChanged();
    }

}

/*
this method below does not delete the selected booked appointment but rather just renove it from the recyclerview and if you load the page again it resurfaces. ensure that it deletes the appointment from the firestore collecction permanently
popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(MenuItem item) {


        if (item.getItemId() == R.id.delete_menu) {
            FirebaseFirestore.getInstance().collection("bookedAppointments")
                    .document(appointment.getConfirmingUserId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(v.getContext(), "Appointment Deleted", Toast.LENGTH_SHORT).show();

                            appointments.remove(appointment);
                            notifyDataSetChanged();
                            // make the card disappear after being deleted
                            holder.linearLayout.setVisibility(View.GONE);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(v.getContext(), "Failed to delete appointment", Toast.LENGTH_SHORT).show();
                            Log.e("DeleteAppointment", "Failed to delete appointment", e);
                        }
                    });
            return true;
        }
        return false;
    }
});

        return false;
correct the method */