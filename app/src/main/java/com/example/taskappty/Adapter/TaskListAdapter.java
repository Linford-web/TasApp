package com.example.taskappty.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.R;
import com.example.taskappty.doneTasks;
import com.example.taskappty.model.TaskModel;
import com.example.taskappty.viewTasks;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private static ArrayList<TaskModel> taskDataset;


    private boolean isTeacher, isStudent;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskNameTv, taskStatusTv;
        LinearLayout containerll;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            taskNameTv = view.findViewById(R.id.taskNameTv);
            taskStatusTv = view.findViewById(R.id.taskStatusTv);
            containerll =  view.findViewById(R.id.containerll);

        }

    }

    public TaskListAdapter(ArrayList<TaskModel> dataSet) {
        this.taskDataset = dataSet;

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        TaskModel task = taskDataset.get(position);

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.taskNameTv.setText(taskDataset.get(position).getTaskName());
        viewHolder.taskStatusTv.setText(taskDataset.get(position).getTaskStatus());

        String status=taskDataset.get(position).getTaskStatus();

        //change  color of status as written
        if (status.equalsIgnoreCase("pending"))
        {
            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#FFFF00"));
        }
        else if (status.equalsIgnoreCase("completed")) {
            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#00FF00"));
        }
        else {
            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#0000FF"));
        }

        //click once to view the task details
        viewHolder.containerll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task != null) {
                    Intent intent = new Intent(v.getContext(), viewTasks.class);
                    intent.putExtra("TaskModel", taskDataset.get(position));
                    v.getContext().startActivity(intent);

                } else {
                    // Log or display a message indicating that the task is null
                    Log.e("TaskListAdapter", "Clicked item has a null task");
                }
            }
        });

        viewHolder.containerll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), viewHolder.containerll);
                popupMenu.inflate(R.menu.taskmenu);


                if (isStudent) {
                    // Current user is a student
                    popupMenu.getMenu().findItem(R.id.markDone).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.deleteMenu).setVisible(true);
                    Log.e("popupmenu", "user not defined");
                } else {
                    // Current user is a teacher
                    popupMenu.getMenu().findItem(R.id.markDone).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.deleteMenu).setVisible(false);
                }

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        // Deal with the delete logic of the menu items
                        if (item.getItemId() == R.id.deleteMenu)
                        {
                            FirebaseFirestore.getInstance().collection("Tasks")
                                    .document(task.getTaskId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(v.getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();

                                            taskDataset.remove(task);
                                            notifyDataSetChanged();
                                            // make the card disappear after being deleted
                                            //viewHolder.containerll.setVisibility(View.GONE);

                                        }
                                    });
                        }

                        // Logic to support tasks marked as done
                        if (item.getItemId()==R.id.markDone){

                            TaskModel completedTask = taskDataset.get(position);
                            completedTask.setTaskStatus("COMPLETED");

                            // Set the user ID who marked the task as done
                            completedTask.setDoneTaskUserId(FirebaseAuth.getInstance().getUid());

                            FirebaseFirestore.getInstance().collection("Tasks")
                                    .document(task.getTaskId())
                                    .set(completedTask)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(v.getContext(), "Task Marked as COMPLETED", Toast.LENGTH_SHORT).show();
                                            viewHolder.containerll.setVisibility(View.GONE);

                                            // move marked as done task tasks to done task activity
                                            Intent intent=new Intent(v.getContext(), doneTasks.class);
                                            intent.putExtra("CompletedTask", String.valueOf(completedTask));
                                            v.getContext().startActivity(intent);
                                            notifyDataSetChanged();

                                        }
                                    });

                            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#00FF00"));
                            viewHolder.taskStatusTv.setText("COMPLETED");
                        }

                        return false;
                    }
                });

                return false;
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskDataset.size();
    }

    public void filterList(ArrayList<TaskModel> filteredList){
        taskDataset.clear();  // Clear the existing data
        taskDataset.addAll(filteredList);  // Add the filtered data
        notifyDataSetChanged();

    }
}