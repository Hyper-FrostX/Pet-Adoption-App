package com.example.pet_adoption;

public class PetModel implements java.io.Serializable {
    private String petName;
    private String breed;
    private String age;
    private String gender;
    private String shelterName;
    private String imageUrl;
    private String description;
    private String petType;
    private String petId;
    private String shelterId;

    // Empty constructor required for Firebase Firestore
    public PetModel() {
    }

    // Full constructor
    public PetModel(String petName, String breed, String age, String gender, String shelterName, String imageUrl, String description, String petType, String petId, String shelterId) {
        this.petName = petName;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.shelterName = shelterName;
        this.imageUrl = imageUrl;
        this.description = description;
        this.petType = petType;
        this.petId = petId;
        this.shelterId = shelterId;
    }

    // Getters and Setters
    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getShelterId() {
        return shelterId;
    }

    public void setShelterId(String shelterId) {
        this.shelterId = shelterId;
    }
}
