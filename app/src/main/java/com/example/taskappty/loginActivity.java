package com.example.taskappty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {


    TextView registerText, forgot_password;
    EditText user_email, user_password;
    Button loginBtn;
    ProgressBar progressbar;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerText = findViewById(R.id.registerText);
        progressbar = findViewById(R.id.progressbar);
        loginBtn = findViewById(R.id.loginBtn);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        forgot_password = findViewById(R.id.forgot_password);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        //checkUserAccessLevel(fAuth.getUid());


        // handle the register text and direct you to register activity
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, registerActivity.class));
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                checkField(user_email);
                checkField(user_password);
                if (valid){
                    fAuth.signInWithEmailAndPassword(user_email.getText().toString(), user_password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    Toast.makeText(loginActivity.this, "logged In Successful", Toast.LENGTH_SHORT).show();

                                    //setLoggedInFlag();
                                    checkUserAccessLevel(Objects.requireNonNull(authResult.getUser()).getUid());
                                    progressbar.setVisibility(View.GONE);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.GONE);
                                }
                            });
                }


            }
        });

        // Deals with the reset password logic
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your Email to Receive Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(loginActivity.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginActivity.this, "Error! Resent Link Not Sent."+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });


    }


    private void setLoggedInFlag() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("LoggedIn",true);
        editor.apply();
    }

    // check currently logged in user making them not login again if they exit app
    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);

        //Extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess:" + documentSnapshot.getData());
                //identify Use Access Level

                if (documentSnapshot.getString("isTeacher") !=null){

                    // user is Teacher
                    startActivity(new Intent(getApplicationContext(), teacherDash.class));
                    finish();

                }
                if (documentSnapshot.getString("isStudent") !=null){
                    startActivity(new Intent(getApplicationContext(), studentDash.class));
                    finish();
                }

            }
        });

    }

    private void checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
    }

}