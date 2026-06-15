package com.example.pet_adoption;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<PetModel> petList;
    private OnPetClickListener listener;

    public interface OnPetClickListener {
        void onPetClick(PetModel pet);
    }

    public void setOnPetClickListener(OnPetClickListener listener) {
        this.listener = listener;
    }

    public PetAdapter(List<PetModel> petList) {
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet_card, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetModel pet = petList.get(position);
        holder.bind(pet, listener);
    }

    @Override
    public int getItemCount() {
        return petList != null ? petList.size() : 0;
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPetImage;
        private final TextView tvPetName, tvBreed, tvAge, tvGender, tvShelter;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPetImage = itemView.findViewById(R.id.petImage);
            tvPetName = itemView.findViewById(R.id.petName);
            tvBreed = itemView.findViewById(R.id.petBreed);
            tvAge = itemView.findViewById(R.id.petAge);
            tvGender = itemView.findViewById(R.id.petGender);
            tvShelter = itemView.findViewById(R.id.shelterName);
        }

        public void bind(final PetModel pet, final OnPetClickListener listener) {
            tvPetName.setText(pet.getPetName());
            tvBreed.setText(pet.getBreed());
            tvAge.setText(pet.getAge());
            tvGender.setText(pet.getGender());
            tvShelter.setText(pet.getShelterName());

            Glide.with(itemView.getContext())
                    .load(pet.getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_paw)
                    .error(R.drawable.ic_paw)
                    .into(ivPetImage);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), PetDetailActivity.class);
                intent.putExtra("petName", pet.getPetName());
                intent.putExtra("breed", pet.getBreed());
                intent.putExtra("age", pet.getAge());
                intent.putExtra("gender", pet.getGender());
                intent.putExtra("shelterName", pet.getShelterName());
                intent.putExtra("imageUrl", pet.getImageUrl());
                intent.putExtra("description", pet.getDescription());
                intent.putExtra("petId", pet.getPetId());
                intent.putExtra("shelterId", pet.getShelterId());
                v.getContext().startActivity(intent);

                if (listener != null) {
                    listener.onPetClick(pet);
                }
            });
        }
    }
}
