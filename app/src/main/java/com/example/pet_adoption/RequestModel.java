package com.example.pet_adoption;

import com.google.firebase.firestore.DocumentId;
import java.util.Date;

public class RequestModel {
    @DocumentId
    private String requestId;
    private String applicantName;
    private String email;
    private String phoneNumber;
    private String address;
    private String reason;
    private String petName;
    private Date requestDate;
    private String shelterName;
    private String shelterId;
    private String status; // Pending, Approved, Rejected

    public RequestModel() {
        // Required for Firebase
    }

    public RequestModel(String applicantName, String email, String phoneNumber, String address, String reason, String petName, Date requestDate, String shelterName, String shelterId, String status) {
        this.applicantName = applicantName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.reason = reason;
        this.petName = petName;
        this.requestDate = requestDate;
        this.shelterName = shelterName;
        this.shelterId = shelterId;
        this.status = status;
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }

    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }

    public String getShelterId() { return shelterId; }
    public void setShelterId(String shelterId) { this.shelterId = shelterId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
