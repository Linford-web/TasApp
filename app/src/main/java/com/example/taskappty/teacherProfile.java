package com.example.taskappty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class teacherProfile extends AppCompatActivity {
    TextView uname, u_id, u_email;
    ImageView back;
    Button logout;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        back = findViewById(R.id.back_arrow);
        uname = findViewById(R.id.prof_name);
        u_id = findViewById(R.id.prof_id);
        u_email = findViewById(R.id.prof_email);
        logout = findViewById(R.id.logoutBtn);

        // Get the current user ID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            // Fetch user details from Firestore "Users" collection
            fStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Retrieve user details
                                String userName = document.getString("name");
                                String userNumber = document.getString("number");
                                String userEmail = document.getString("email");

                                // Set the user details in the respective TextViews
                                uname.setText(userName);
                                u_id.setText(userNumber);
                                u_email.setText(userEmail);
                            } else {
                                Toast.makeText(teacherProfile.this, "User document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(teacherProfile.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // set back arrow to direct user to previous page.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), teacherDash.class);
                startActivity(intent);
                finish();
            }
        });

        // Logic For Logout Button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }
}