package com.example.taskappty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class studentDash extends AppCompatActivity {

    ImageView student_task, student_appointment, done_task, booked;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;
    TextView get_user_name;
    TextView userNameTv;
    CircleImageView userprofile;
    ImageView image1, image2, image3;
    LinearLayout linearLayout;



    private int[] imageIds = {R.drawable.dash_image2, R.drawable.dash_image3, R.drawable.dash_image4};
    private int currentIndex = 0;

    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            switchImages();
            handler.postDelayed(this, 2000); // Switch image every 2 seconds
        }
    };

    private void switchImages() {

        currentIndex = (currentIndex + 1) % imageIds.length;
        image1.setImageResource(imageIds[currentIndex]);
        image2.setImageResource(imageIds[(currentIndex + 1) % imageIds.length]);
        image3.setImageResource(imageIds[(currentIndex + 2) % imageIds.length]);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dash);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        userprofile = findViewById(R.id.userProfileTv);

        userNameTv = findViewById(R.id.get_name);
        student_task= findViewById(R.id.student_task);
        get_user_name = findViewById(R.id.get_user_name);
        student_appointment = findViewById(R.id.student_appointment);
        done_task = findViewById(R.id.done_task);
        booked = findViewById(R.id.booked);
        image1 = findViewById(R.id.dash_image1);
        image2 = findViewById(R.id.dash_image2);
        image3 = findViewById(R.id.dash_image3);
        linearLayout = findViewById(R.id.linearLayout);

        handler = new Handler();

        get_user_name.setText(user.getEmail());

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
                                // Retrieve profile photo URL from Firestore
                                String profileImageUrl = document.getString("profilePicture");
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    // Load profile photo into otherImageView using Glide or any other image loading library
                                    Glide.with(this).load(profileImageUrl).into(userprofile);
                                } else {
                                    // Handle the case when no profile photo is available
                                    Toast.makeText(this, "No profile photo found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "User document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // direct user to Menu
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(studentDash.this, studentProfile.class);
                startActivity(intent);
                finish();
            }
        });

        // direct user to the student Task page
        student_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(studentDash.this, studentTask.class);
                startActivity(intent);
            }
        });

        // direct user to the Student Done tasks page
        done_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(studentDash.this, doneTasks.class);
                startActivity(intent);
            }
        });

        // direct user to the Student Appointment page
        student_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(studentDash.this, studentAppointment.class);
                startActivity(intent);
            }
        });

        // direct user to the Booked Appointments page
        booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(studentDash.this, bookedAppointment.class);
                startActivity(intent);
            }
        });

        // get user name and display it
        fetchUserName();

    }


    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable, 2000); // Start the auto-switching of images
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable); // Stop the auto-switching when the activity is stopped
    }

    // Ensure that the dashboard does not get you back to the login activity but exit app
    @Override
    public void finish() {
        super.finish();
        finishAffinity();
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
                                    userNameTv.setText(userName);

                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                                Toast.makeText(studentDash.this, "Error fetching user name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}