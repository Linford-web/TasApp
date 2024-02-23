package com.example.taskappty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskappty.model.TaskModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class studentTask extends AppCompatActivity {

        RecyclerView taskRv;
        TextView userNameTv;
        TextView back;
        ArrayList<TaskModel> pendingTasks;
        TaskListAdapter taskListAdapter;
        SearchView searchView;
        FirebaseFirestore fStore;
        FirebaseAuth fAuth;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_task);

            fStore = FirebaseFirestore.getInstance();
            fAuth = FirebaseAuth.getInstance();

            userNameTv = findViewById(R.id.get_user_name);
            searchView = findViewById(R.id.search_view);
            back = findViewById(R.id.back_box);



            taskRv = findViewById(R.id.taskListRv);
            taskRv.setLayoutManager(new LinearLayoutManager(this));

            pendingTasks = new ArrayList<>();
            taskListAdapter = new TaskListAdapter(new ArrayList<>(pendingTasks));
            taskListAdapter = new TaskListAdapter(pendingTasks);
            taskRv.setAdapter(taskListAdapter);


            // fetch all pending tasks
            fetchPendingTasks();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText);

                    return true;
                }
            });

            // set the floating back button to return user to Dash
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            // get user name and display it
            fetchUserName();

        }

    private void filter(String newText) {

            /*
        if (newText.isEmpty()) {
            // If search text is empty, show all tasks
            taskListAdapter.filterList(new ArrayList<>(pendingTasks));
        } else {
            // Filter the tasks based on the search text
            ArrayList<TaskModel> filteredList = new ArrayList<>();
            for (TaskModel item : pendingTasks) {
                if (item.getTaskName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(item);
                }
            }*/

            ArrayList<TaskModel> filteredList = new ArrayList<>();
            for (TaskModel item : pendingTasks){
                if (item.getTaskName().toLowerCase().contains(newText.toLowerCase())){
                    filteredList.add(item);
                }
            }

            taskListAdapter.filterList(filteredList);
    }

    private void fetchPendingTasks() {

            fStore.collection("Tasks")
                    .whereEqualTo("taskStatus", "PENDING")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                pendingTasks.clear(); // Clear existing data before adding new data
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAGUS", document.getId() + " => " + document.getData());

                                    TaskModel taskModel = document.toObject(TaskModel.class);
                                    taskModel.setTaskId(document.getId());

                                    pendingTasks.add(taskModel);
                                }
                                taskListAdapter.notifyDataSetChanged(); // Notify the adapter after updating the dataset
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                Toast.makeText(studentTask.this, "Error fetching PENDING Tasks", Toast.LENGTH_SHORT).show();
                            }

                            if (!isFinishing()) {
                                taskListAdapter.notifyDataSetChanged(); // Notify the adapter after updating the dataset
                            }
                        }
                    });

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
                                Toast.makeText(studentTask.this, "Error fetching user name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}