package com.example.taskappty;

import com.example.taskappty.model.AppointmentModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppointmentManager {

    FirebaseFirestore fStore;
    CollectionReference appointments;

    public AppointmentManager() {
        fStore = FirebaseFirestore.getInstance();
        appointments = FirebaseFirestore.getInstance().collection("Appointments");
    }

    public void createAppointment(AppointmentModel appointment) {
        // Add the appointment to FireStore
        appointments.add(appointment)
                .addOnSuccessListener(documentReference -> {
                    // Handle success if needed

                })
                .addOnFailureListener(e -> {
                    // Handle failure if needed
                });
    }

    public CollectionReference getAppointmentsCollection(String appointments) {
        return FirebaseFirestore.getInstance().collection(appointments);
    }
}
