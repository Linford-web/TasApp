package com.example.taskappty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.taskappty.Adapter.TaskListAdapter;
import com.example.taskappty.model.TaskModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class doneTasks extends AppCompatActivity {

    RecyclerView donetasks;
    ImageView back;
    ArrayList<TaskModel> doneTask;
    TaskListAdapter taskListAdapter;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_tasks);

        back = findViewById(R.id.back_arrow);
        donetasks = findViewById(R.id.doneTaskRv);

        // WeInitialize Firebase as fStore
        fStore = FirebaseFirestore.getInstance();

        ArrayList<TaskModel> pendingTasks;

        doneTask = new ArrayList<>();
        taskListAdapter = new TaskListAdapter(doneTask);
        donetasks.setAdapter(taskListAdapter);
        // Set up RecyclerView
        donetasks.setLayoutManager(new LinearLayoutManager(this));
        donetasks.setAdapter(taskListAdapter);

        // Ensure back arrow directs user back to previous activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load done tasks from FireStore
        getDoneTasks();

    }

    private void getDoneTasks() {

        String currentUserId = FirebaseAuth.getInstance().getUid();

        fStore.collection("Tasks")
                .whereEqualTo("taskStatus", "COMPLETED")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        doneTask.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TaskModel taskModel = document.toObject(TaskModel.class);
                            taskModel.setTaskId(document.getId());

                            // Check if the current user is a student and marked the task as done or if the current user is a teacher who created the task
                            boolean isStudent=true;
                            boolean isTeacher = true;

                            assert currentUserId != null;
                            if (currentUserId.equals(taskModel.getDoneTaskUserId()) || currentUserId.equals(taskModel.getUserId())) {
                                doneTask.add(taskModel);
                            }
                        }
                        taskListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(doneTasks.this, "Error loading done tasks", Toast.LENGTH_SHORT).show();
                    }
                });

        // ensure back arrow directs user back to precious activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}