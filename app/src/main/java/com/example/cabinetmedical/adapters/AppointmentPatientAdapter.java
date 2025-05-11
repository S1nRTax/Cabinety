package com.example.cabinetmedical.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.data.local.entity.Appointment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AppointmentPatientAdapter extends RecyclerView.Adapter<AppointmentPatientAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private OnAppointmentActionListener listener;

    public interface OnAppointmentActionListener {
        void onAcceptAppointment(Appointment appointment);
        void onRejectAppointment(Appointment appointment);
        void onCancelAppointment(Appointment appointment);
    }

    public AppointmentPatientAdapter(OnAppointmentActionListener listener) {
        this.listener = listener;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_patient, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a", Locale.getDefault());

        holder.tvAppointmentTime.setText(sdf.format(appointment.getAppointmentTime()));
        holder.tvAppointmentPurpose.setText(appointment.getPurpose());
        holder.tvAppointmentStatus.setText("Status: " + appointment.getStatus());

        switch(appointment.getStatus()) {
            case Appointment.STATUS_PENDING:
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnReject.setVisibility(View.VISIBLE);
                holder.btnCancel.setVisibility(View.GONE);
                break;
            case Appointment.STATUS_CONFIRMED:
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnCancel.setVisibility(View.VISIBLE);
                break;
            case Appointment.STATUS_REJECTED:
            case Appointment.STATUS_CANCELLED:
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnCancel.setVisibility(View.GONE);
                break;
        }

        // Set click listeners
        holder.btnAccept.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAcceptAppointment(appointment);
            }
        });

        holder.btnReject.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRejectAppointment(appointment);
            }
        });

        holder.btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelAppointment(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments != null ? appointments.size() : 0;
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppointmentTime, tvAppointmentPurpose, tvAppointmentStatus;
        Button btnAccept, btnReject, btnCancel;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvAppointmentPurpose = itemView.findViewById(R.id.tvAppointmentPurpose);
            tvAppointmentStatus = itemView.findViewById(R.id.tvAppointmentStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
