package com.example.taskappty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskappty.model.TaskModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class viewTasks extends AppCompatActivity {


    TextView task_name, task_description;
    ImageView back;

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        fStore = FirebaseFirestore.getInstance();

        // Assuming you have TextViews in your layout to display task details
        task_name = findViewById(R.id.showTaskName);
        task_description = findViewById(R.id.showTaskDescription);
        back = findViewById(R.id.back_arrow);

        // Retrieve the TaskModel object from the intent
        TaskModel taskModel = getIntent().getParcelableExtra("TaskModel");

        if (taskModel != null) {
            // Set the task name and description from the TaskModel
            task_name.setText(taskModel.getTaskName());
            task_description.setText(taskModel.getTaskDescription());

            // Fetch additional details from Firebase if needed
            fetchAdditionalDetails(taskModel.getTaskId());
        }

        // back button logic
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void fetchAdditionalDetails(String taskId) {
        // Assuming you have a "Tasks" collection in your Firestore database
        FirebaseFirestore.getInstance().collection("Tasks")
                .document(taskId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                        }
                    }
                });

    }
}