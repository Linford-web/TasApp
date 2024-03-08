package com.example.taskappty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.Adapter.AppointmentAdapter;
import com.example.taskappty.model.AppointmentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class studentAppointment extends AppCompatActivity {

    TextView back, uNameTv;
    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    CollectionReference appointmentsRef;
    ArrayList<AppointmentModel> appointmentList;
    AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_appointment);



        back = findViewById(R.id.back_box);
        uNameTv = findViewById(R.id.get_user_name);
        recyclerView = findViewById(R.id.showAppointmentsRv);
        fStore = FirebaseFirestore.getInstance();
        appointmentList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(appointmentList);
        recyclerView.setAdapter(appointmentAdapter);
        appointmentsRef = FirebaseFirestore.getInstance().collection("Appointments");



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(appointmentAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // get user name and display it
        fetchUserName();

        // Check for incoming appointment data from createAppointment activity
        checkIncomingIntent();

        loadAppointmentsFromFirebase();
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

    private void fetchUserName() {
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            fStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    String userName = document.getString("name");
                                    // Set the user name in the TextView
                                    uNameTv.setText(userName);

                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                                Toast.makeText(studentAppointment.this, "Error fetching user name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void loadAppointmentsFromFirebase() {
        String appointmentId = FirebaseFirestore.getInstance().toString();
        if (!(appointmentId == null)) {

            CollectionReference appointmentCollection = fStore.collection("Appointments");

            appointmentCollection.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                appointmentList.clear(); // Clear existing data before adding new data
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAGTHEM", document.getId() + " => " + document.getData());

                                    AppointmentModel appointmentModel = document.toObject(AppointmentModel.class);
                                    appointmentModel.setAppointmentId("");
                                    appointmentList.add(appointmentModel);
                                }
                                appointmentAdapter.notifyDataSetChanged(); // Notify the adapter after updating the dataset
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                Toast.makeText(studentAppointment.this, "Error fetching PENDING Tasks", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }

    }
}