package com.example.taskappty.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.R;
import com.example.taskappty.displayTimeGrid;
import com.example.taskappty.model.AppointmentModel;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    ArrayList<AppointmentModel> appointments;

    // ViewHolder class to hold references to views in the item layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewEmail;
        Button bookButton;
        LinearLayout container;

        public ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.textViewName);
            textViewEmail = view.findViewById(R.id.textViewEmail);
            bookButton = view.findViewById(R.id.bookButton);
            container = view.findViewById(R.id.containerll);
        }
    }

    public AppointmentAdapter(ArrayList<AppointmentModel> appointmentList) {
        this.appointments = appointmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppointmentModel appointment = appointments.get(position);

        // Set data to views in the item layout
        holder.textViewName.setText(appointments.get(position).getTeacherName());
        holder.textViewEmail.setText(appointments.get(position).getDayOfWeek());

        //book button when clicked
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appointment != null) {
                    String appointmentDate = appointment.getDayOfWeek();
                    String startTime = appointment.getStartTime();
                    String endTime = appointment.getEndTime();

                    Intent intent = new Intent(v.getContext(), displayTimeGrid.class);
                    intent.putExtra("appointmentDate", appointmentDate);
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("endTime", endTime);
                    v.getContext().startActivity(intent);

                } else {
                    // Log or display a message indicating that the task is null
                    Log.e("TaskListAdapter", "Clicked item has a null task");
                }
            }

        });

    }

    // New method to add an appointment
    public void addAppointment(AppointmentModel appointment) {
        appointments.add(appointment);
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
    @Override
    public int getItemCount() {
        return appointments.size();
    }

}
