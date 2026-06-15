package com.example.pet_adoption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdoptionRequestAdapter extends RecyclerView.Adapter<AdoptionRequestAdapter.ViewHolder> {

    private List<AdoptionRequestModel> requestList;
    private OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onApprove(AdoptionRequestModel request);
        void onReject(AdoptionRequestModel request);
    }

    public AdoptionRequestAdapter(List<AdoptionRequestModel> requestList, OnRequestActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adoption_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdoptionRequestModel request = requestList.get(position);
        holder.tvPetName.setText(request.getPetName());
        holder.tvApplicantName.setText("Applicant: " + request.getApplicantName());
        holder.tvPhone.setText("Phone: " + request.getPhoneNumber());
        holder.tvReason.setText("Reason: " + request.getReason());
        holder.tvStatus.setText("Status: " + request.getStatus());

        if ("pending".equalsIgnoreCase(request.getStatus())) {
            holder.layoutButtons.setVisibility(View.VISIBLE);
            holder.tvStatus.setTextColor(0xFFFF9800); // Orange
        } else {
            holder.layoutButtons.setVisibility(View.GONE);
            if ("approved".equalsIgnoreCase(request.getStatus())) {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPetName, tvApplicantName, tvPhone, tvReason, tvStatus;
        MaterialButton btnApprove, btnReject;
        LinearLayout layoutButtons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvApplicantName = itemView.findViewById(R.id.tvApplicantName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
            layoutButtons = itemView.findViewById(R.id.layoutButtons);
        }
    }
}
