package com.example.pet_adoption;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageRequestsActivity extends AppCompatActivity implements AdoptionRequestAdapter.OnRequestActionListener {

    private RecyclerView rvRequests;
    private AdoptionRequestAdapter adapter;
    private List<AdoptionRequestModel> requestList;
    private ProgressBar progressBar;
    private TextView tvEmptyState;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUserId = mAuth.getCurrentUser().getUid();

        rvRequests = findViewById(R.id.rvRequests);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        requestList = new ArrayList<>();
        adapter = new AdoptionRequestAdapter(requestList, this);

        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        rvRequests.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Requests");
        }

        fetchRequests();
    }

    private void fetchRequests() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("adoptionRequests")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    progressBar.setVisibility(View.GONE);

                    requestList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        AdoptionRequestModel request =
                                doc.toObject(AdoptionRequestModel.class);

                        request.setRequestId(doc.getId());

                        requestList.add(request);
                    }

                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                })
                .addOnFailureListener(e -> {

                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(
                            ManageRequestsActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    private void updateEmptyState() {
        if (requestList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvRequests.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvRequests.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onApprove(AdoptionRequestModel request) {
        updateRequestStatus(request.getRequestId(), "Approved");
    }

    @Override
    public void onReject(AdoptionRequestModel request) {
        updateRequestStatus(request.getRequestId(), "Rejected");
    }

    private void updateRequestStatus(String requestId, String newStatus) {

        db.collection("adoptionRequests")
                .document(requestId)
                .update("status", newStatus)
                .addOnSuccessListener(unused ->
                        Toast.makeText(
                                ManageRequestsActivity.this,
                                "Request " + newStatus,
                                Toast.LENGTH_SHORT
                        ).show())
                .addOnFailureListener(e ->
                        Toast.makeText(
                                ManageRequestsActivity.this,
                                "Failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}