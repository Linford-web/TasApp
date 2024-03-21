package com.example.taskappty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.Adapter.AppointmentAdapter;
import com.example.taskappty.model.AppointmentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class teacherAppointment extends AppCompatActivity {


    RecyclerView recyclerView;
    CollectionReference appointmentsRef;
    ArrayList<AppointmentModel> appointmentList;
    AppointmentAdapter appointmentAdapter;
    FloatingActionButton createAppointment;
    ImageView back;

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_appointment);

        recyclerView = findViewById(R.id.apptRv);
        back = findViewById(R.id.back_arrow);

        appointmentsRef = FirebaseFirestore.getInstance().collection("Appointments");
        appointmentList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(appointmentList);
        createAppointment = findViewById(R.id.addAppointmentBtn);

        fStore = FirebaseFirestore.getInstance();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(appointmentAdapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Check for incoming appointment data from createAppointment activity
        checkIncomingIntent();

        loadAppointmentsFromFirebase();

        createAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), createAppointment.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void checkIncomingIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("appointmentModel")) {
            // Handle incoming appointment data from createAppointment activity
            AppointmentModel newAppointment = intent.getParcelableExtra("appointmentModel");
            if (newAppointment != null) {
                // Add the new appointment to the list and notify the adapter
                appointmentList.add(newAppointment);
                appointmentAdapter.notifyDataSetChanged();
            }
        }
    }

    private void loadAppointmentsFromFirebase() {

        String appointmentId = FirebaseFirestore.getInstance().toString();
        if (!(appointmentId == null)) {
            String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            CollectionReference appointmentCollection = fStore.collection("Appointments");

            Query query = appointmentCollection.whereEqualTo("userId", currentUserId);
            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                appointmentList.clear(); // Clear existing data before adding new data
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAGUS", document.getId() + " => " + document.getData());

                                    AppointmentModel appointmentModel = document.toObject(AppointmentModel.class);
                                    appointmentModel.setAppointmentId("");
                                    appointmentList.add(appointmentModel);
                                }
                                appointmentAdapter.notifyDataSetChanged(); // Notify the adapter after updating the dataset
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                Toast.makeText(teacherAppointment.this, "Error fetching PENDING Tasks", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }
}