package com.example.pet_adoption;

import com.google.firebase.firestore.DocumentId;
import java.util.Date;

public class AdoptionRequestModel {
    @DocumentId
    private String requestId;
    private String petName;
    private String petId;
    private String shelterId;
    private String shelterName;
    private String applicantName;
    private String phoneNumber;
    private String email;
    private String address;
    private String reason;
    private String status; // pending, approved, rejected
    private Date requestDate;

    public AdoptionRequestModel() {
        // Required for Firebase
    }

    public AdoptionRequestModel(String petName, String petId, String shelterId, String shelterName, String applicantName, String phoneNumber, String email, String address, String reason, String status, Date requestDate) {
        this.petName = petName;
        this.petId = petId;
        this.shelterId = shelterId;
        this.shelterName = shelterName;
        this.applicantName = applicantName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.reason = reason;
        this.status = status;
        this.requestDate = requestDate;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
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

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
