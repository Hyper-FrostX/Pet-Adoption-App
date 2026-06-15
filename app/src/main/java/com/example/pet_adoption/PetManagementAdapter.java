package com.example.pet_adoption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class PetManagementAdapter extends RecyclerView.Adapter<PetManagementAdapter.PetViewHolder> {

    private List<PetModel> petList;
    private OnPetActionListener listener;

    public interface OnPetActionListener {
        void onEdit(PetModel pet);
        void onDelete(PetModel pet);
    }

    public PetManagementAdapter(List<PetModel> petList, OnPetActionListener listener) {
        this.petList = petList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetModel pet = petList.get(position);
        holder.tvPetName.setText(pet.getPetName());
        holder.tvBreed.setText(pet.getBreed());
        holder.tvAgeType.setText(pet.getAge() + " yrs | " + pet.getPetType());

        Glide.with(holder.itemView.getContext())
                .load(pet.getImageUrl())
                .placeholder(R.drawable.ic_paw)
                .into(holder.ivPetImage);

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(pet));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(pet));
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPetImage;
        TextView tvPetName, tvBreed, tvAgeType;
        MaterialButton btnEdit, btnDelete;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPetImage = itemView.findViewById(R.id.ivPetImage);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvBreed = itemView.findViewById(R.id.tvBreed);
            tvAgeType = itemView.findViewById(R.id.tvAgeType);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
