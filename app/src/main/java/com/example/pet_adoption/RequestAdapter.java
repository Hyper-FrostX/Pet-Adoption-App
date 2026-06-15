package com.example.pet_adoption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<RequestModel> requestList;
    private OnRequestActionListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public interface OnRequestActionListener {
        void onApprove(RequestModel request);
        void onReject(RequestModel request);
    }

    public RequestAdapter(List<RequestModel> requestList, OnRequestActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestModel request = requestList.get(position);
        
        holder.tvApplicantName.setText(request.getApplicantName());
        holder.tvPetName.setText("Pet: " + request.getPetName());
        holder.tvEmail.setText("Email: " + request.getEmail());
        holder.tvPhone.setText("Phone: " + request.getPhoneNumber());
        holder.tvReason.setText("Reason: " + request.getReason());
        
        if (request.getRequestDate() != null) {
            holder.tvRequestDate.setText("Date: " + dateFormat.format(request.getRequestDate()));
        }

        String status = request.getStatus();
        holder.tvStatus.setText("Status: " + status);

        if ("Pending".equalsIgnoreCase(status)) {
            holder.layoutButtons.setVisibility(View.VISIBLE);
            holder.tvStatus.setTextColor(0xFFFF9800); // Orange
        } else {
            holder.layoutButtons.setVisibility(View.GONE);
            if ("Approved".equalsIgnoreCase(status)) {
                holder.tvStatus.setTextColor(0xFF4CAF50); // Green
            } else {
                holder.tvStatus.setTextColor(0xFFD32F2F); // Red
            }
        }

        holder.btnApprove.setOnClickListener(v -> listener.onApprove(request));
        holder.btnReject.setOnClickListener(v -> listener.onReject(request));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvApplicantName, tvPetName, tvEmail, tvPhone, tvReason, tvRequestDate, tvStatus;
        MaterialButton btnApprove, btnReject;
        LinearLayout layoutButtons;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvApplicantName = itemView.findViewById(R.id.tvApplicantName);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvRequestDate = itemView.findViewById(R.id.tvRequestDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
            layoutButtons = itemView.findViewById(R.id.layoutButtons);
        }
    }
}
