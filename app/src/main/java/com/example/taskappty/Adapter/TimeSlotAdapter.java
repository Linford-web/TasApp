package com.example.taskappty.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappty.R;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {
    Context context;
    List<String> timeSlots;
    String selectedTimeSlot;


    public TimeSlotAdapter(Context context, List<String> timeSlots) {
        this.context = context;
        this.timeSlots = timeSlots;
    }

    public String getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_slots, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String timeSlot = timeSlots.get(position);
        holder.tvTimeSlot.setText(timeSlot);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                selectedTimeSlot = timeSlot;
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeSlot;
        TextView tvAppointmentStatus;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeSlot = itemView.findViewById(R.id.tvTimeSlot);
        }
    }
}
