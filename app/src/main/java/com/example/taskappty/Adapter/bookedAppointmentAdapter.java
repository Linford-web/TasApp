package com.example.taskappty.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.R;
import com.example.taskappty.model.Appointment;

import java.util.List;

public class bookedAppointmentAdapter extends RecyclerView.Adapter<bookedAppointmentAdapter.ViewHolder> {
    List<Appointment> appointments;
    OnItemClickListener onItemClickListener;

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public bookedAppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booked_appointments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        // Bind appointment data to the ViewHolder
        holder.dateTextView.setText(appointment.getAppointmentDate());
        holder.timeTextView.setText(appointment.getSelectedTimeSlot());

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void setData(List<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.textDate);
            timeTextView = itemView.findViewById(R.id.textTime);

            // Set up click listener
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(appointments.get(position));
                }
            });

        }
    }

}
