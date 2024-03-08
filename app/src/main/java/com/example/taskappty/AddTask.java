package com.example.taskappty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskappty.model.TaskModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddTask extends AppCompatActivity {

    ImageView back;
    Button save;
    ProgressBar progressBar;
    EditText utaskname, taskDescriptiom;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        fStore = FirebaseFirestore.getInstance();

        back = findViewById(R.id.addTask_backBtn);
        taskDescriptiom = findViewById(R.id.taskDescription);
        save = findViewById(R.id.addTaskBtn);
        progressBar = findViewById(R.id.progressbar);
        utaskname=findViewById(R.id.inputTaskName);

        // Logic to take care of back arrow image || directs teacher to task page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), teacherTask.class));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String taskname=utaskname.getText().toString().trim();
                String taskDescription= taskDescriptiom.getText().toString().trim();

                if (!(taskname == null))
                {
                    Toast.makeText(AddTask.this, taskname, Toast.LENGTH_SHORT).show();

                    //save task details to FireStore.
                    TaskModel taskModel=new TaskModel("",taskname,"PENDING", FirebaseAuth.getInstance().getUid(), taskDescription);

                    fStore.collection("Tasks")
                            .add(taskModel)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("TAG","Document added with ID: " + documentReference.getId());

                                    // Ensures that the success_layout disappears after 3 seconds
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent =new Intent(AddTask.this, teacherTask.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    },1000);

                                    //make success layout appear after Task is successfully added
                                    findViewById(R.id.successlayout).setVisibility(View.VISIBLE);
                                    // make add task layout to disappear
                                    findViewById(R.id.addtaskLayout).setVisibility(View.GONE);
                                    //make top navigation disappear
                                    findViewById(R.id.top_bar_nav).setVisibility(View.GONE);
                                    // make under view disappear
                                    findViewById(R.id.under_view).setVisibility(View.GONE);

                                    progressBar.setVisibility(View.GONE);


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("LOG","Error Adding Document",e);
                                    progressBar.setVisibility(View.GONE);

                                }
                            });
                }
            }
        });


    }

}