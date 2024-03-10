package com.example.taskappty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.Adapter.TaskListAdapter;
import com.example.taskappty.model.TaskModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class teacherTask extends AppCompatActivity {

    RecyclerView taskRv;
    ArrayList<TaskModel> dataList=new ArrayList<>();

    TaskListAdapter taskListAdapter;
    CircleImageView userprofile;

    FirebaseFirestore fStore;
    TextView userNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_task);

        fStore = FirebaseFirestore.getInstance();

        taskRv=findViewById(R.id.taskListRv);
        userprofile = findViewById(R.id.userProfileTv);
        userNameTv = findViewById(R.id.get_user_name);

        //check if user is null or not and append User Name on the text view
        fetchUserName();
        dataList = new ArrayList<>();
        taskListAdapter = new TaskListAdapter(new ArrayList<>(dataList));
        //dataList.add(new TaskModel("testId", "Demo Task", "completed"));
        taskListAdapter=new TaskListAdapter(dataList);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        taskRv.setLayoutManager(layoutManager);
        taskRv.setAdapter(taskListAdapter);


        // Add Task Button direct Teacher to Add Task Activity
        findViewById(R.id.add_taskFBN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddTask.class));
                finish();
            }
        });

        fetchPendingTasks();

    }
    
    private void fetchPendingTasks() {
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            fStore.collection("Tasks")
                    // enables task created with the user Id to be displayed
                    .whereEqualTo("userId", FirebaseAuth.getInstance().getUid())
                    .whereEqualTo("taskStatus", "PENDING")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                dataList.clear(); // Clear existing data before adding new data
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());

                                    TaskModel taskModel = document.toObject(TaskModel.class);
                                    taskModel.setTaskId(document.getId());
                                    dataList.add(taskModel);
                                }
                                taskListAdapter.notifyDataSetChanged(); // Notify the adapter after updating the dataset
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                Toast.makeText(teacherTask.this, "Error fetching PENDING Tasks", Toast.LENGTH_SHORT).show();
                            }

                            if (!isFinishing()) {
                                taskListAdapter.notifyDataSetChanged(); // Notify the adapter after updating the dataset
                            }
                        }
                    });
        }
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
                                    userNameTv.setText(userName); // Set the user name in the TextView
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                                Toast.makeText(teacherTask.this, "Error fetching user name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}