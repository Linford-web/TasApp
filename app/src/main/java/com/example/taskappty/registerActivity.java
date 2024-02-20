package com.example.taskappty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {


    TextView loginText;
    EditText reg_number, user_name, user_email, user_password;
    Button registerBtn;
    ProgressBar progressbar;
    CheckBox isTeacherBox, isStudentBox;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        loginText = findViewById(R.id.loginText);
        progressbar = findViewById(R.id.progressbar);
        reg_number = findViewById(R.id.reg_number);
        user_name = findViewById(R.id.user_name);
        registerBtn = findViewById(R.id.registerBtn);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        isTeacherBox = findViewById(R.id.isTeacher);
        isStudentBox = findViewById(R.id.isStudent);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // check boxes logic

        isStudentBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    isTeacherBox.setChecked(false);
                }

            }
        });
        isTeacherBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    isStudentBox.setChecked(false);
                }
            }
        });

        // handle the register text and direct you to register activity
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registerActivity.this, loginActivity.class));

            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(user_name);
                checkField(user_email);
                checkField(reg_number);
                checkField(user_password);

                //checkbox validation
                if (!(isTeacherBox.isChecked() || isStudentBox.isChecked())){
                    Toast.makeText(registerActivity.this, "Select User Type", Toast.LENGTH_SHORT).show();
                    return;
                }



                if (valid){

                    progressbar.setVisibility(View.VISIBLE);
                    // start the user registration process
                    fAuth.createUserWithEmailAndPassword(user_email.getText().toString(),user_password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    FirebaseUser user =fAuth.getCurrentUser();

                                    Toast.makeText(registerActivity.this, "Account Created", Toast.LENGTH_SHORT).show();


                                    DocumentReference df = fStore.collection("Users").document(user.getUid());
                                    Map<String,Object> User= new HashMap<>();
                                    User.put("name", user_name.getText().toString());
                                    User.put("number", reg_number.getText().toString());
                                    User.put("email", user_email.getText().toString());

                                    // specify Access Level
                                    if (isTeacherBox.isChecked()){
                                        User.put("isTeacher", "teacher");
                                    }
                                    if (isStudentBox.isChecked()){
                                        User.put("isStudent", "student");
                                    }

                                    df.set(User);

                                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
                                    finish();
                                    progressbar.setVisibility(View.GONE);
                                }


                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(registerActivity.this, "Failed to Create Account!", Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.GONE);
                                }
                            });



                }
            }
        });


    }

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

}