package com.example.taskappty.Adapter;

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
import com.example.taskappty.model.Appointment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class bookedAppointmentAdapter extends RecyclerView.Adapter<bookedAppointmentAdapter.ViewHolder> {
    List<Appointment> appointments;
    OnItemClickListener onItemClickListener;
    boolean teacher;


    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public bookedAppointmentAdapter(List<Appointment> appointments, boolean teacher) {
        this.appointments = appointments;
        this.teacher = teacher;
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

        if(teacher) {
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.linearLayout);
                    popupMenu.inflate(R.menu.appointmentmenu);

                    popupMenu.getMenu().findItem(R.id.delete_menu).setVisible(true);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {


                            if (item.getItemId() == R.id.delete_menu) {
                                FirebaseFirestore.getInstance().collection("bookedAppointments")
                                        .document( )
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(v.getContext(), "Appointment Deleted", Toast.LENGTH_SHORT).show();

                                                appointments.remove(appointment);
                                                notifyDataSetChanged();
                                                // make the card disappear after being deleted
                                                holder.linearLayout.setVisibility(View.GONE);

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(v.getContext(), "Failed to delete appointment", Toast.LENGTH_SHORT).show();
                                                Log.e("DeleteAppointment", "Failed to delete appointment", e);
                                            }
                                        });
                                return true;
                            }
                            return false;
                        }
                    });

                    return false;
                }
            });
        }

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
        public LinearLayout linearLayout;
        TextView dateTextView, timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.textDate);
            timeTextView = itemView.findViewById(R.id.textTime);
            linearLayout = itemView.findViewById(R.id.containerLayout);

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
