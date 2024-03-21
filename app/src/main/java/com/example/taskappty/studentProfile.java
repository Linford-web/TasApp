package com.example.taskappty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class studentProfile extends AppCompatActivity {
    TextView uname, u_id, u_email;
    ImageView back, imageView;
    Button logout;
    Button selectPicture;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser fUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        back = findViewById(R.id.back_arrow);
        uname = findViewById(R.id.prof_name);
        u_id = findViewById(R.id.prof_id);
        u_email = findViewById(R.id.prof_email);
        logout = findViewById(R.id.logoutBtn);

        selectPicture = findViewById(R.id.updateProfileBtn);
        imageView = findViewById(R.id.imageView);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Get the current user ID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            // Fetch user details from FireStore "Users" collection
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
                                Toast.makeText(studentProfile.this, "User document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(studentProfile.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

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
                                    // Load profile photo into imageView using Glide or any other image loading library
                                    Glide.with(this).load(profileImageUrl).into(imageView);
                                } else {
                                    // Handle the case when no profile photo is available
                                    Toast.makeText(studentProfile.this, "No profile photo found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(studentProfile.this, "User document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(studentProfile.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        // set back arrow to direct user to previous page.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), studentDash.class);
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

        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(studentProfile.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);

            // Get a reference to the Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            // Create a reference to 'profile_pictures/<FILENAME>.jpg'
            final StorageReference profileRef = storageRef.child("profile_pictures/" + FirebaseAuth.getInstance().getUid() + ".jpg");

            // Upload file to Firebase Storage
                assert uri != null;
                profileRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Get the download URL of the uploaded file
                                    String imageUrl = uri.toString();
                                    // Update the user's profile picture in Firestore
                                    fStore.collection("Users")
                                            .document(FirebaseAuth.getInstance().getUid())
                                            .update("profilePicture", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(studentProfile.this, "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(e -> {
                                                // Handle unsuccessful uploads
                                                Toast.makeText(studentProfile.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                                            });

                                }
                            });
                        }
                    });
        } else {
            // Handle the case when no image is selected or user canceled the operation
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }
}