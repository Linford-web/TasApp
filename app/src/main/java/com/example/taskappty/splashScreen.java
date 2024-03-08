package com.example.taskappty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class splashScreen extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (fAuth.getCurrentUser() !=null){
                    startDashboardActivity();
                }
                else {
                    Intent intent =new Intent(splashScreen.this, loginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);


    }
    @Override
    protected void onStart() {
        super.onStart();


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startDashboardActivity();
        }
    }
    private void startDashboardActivity() {
        DocumentReference fStore = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        fStore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("isTeacher")!=null){

                    startActivity(new Intent(getApplicationContext(), teacherDash.class));
                    finish();
                } else if (documentSnapshot.getString("isStudent") !=null){
                    startActivity(new Intent(getApplicationContext(), studentDash.class));
                    finish();
                } else {
                    Toast.makeText(splashScreen.this, "Error", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
                    finish();
                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), loginActivity.class));
                finish();
            }
        });
    }
}