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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewRequestsActivity extends AppCompatActivity implements RequestAdapter.OnRequestActionListener {

    private RecyclerView rvRequests;
    private RequestAdapter adapter;
    private List<RequestModel> requestList;
    private ProgressBar progressBar;
    private TextView tvEmptyState;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        
        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }
        currentUserId = mAuth.getCurrentUser().getUid();

        rvRequests = findViewById(R.id.rvRequests);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        requestList = new ArrayList<>();
        adapter = new RequestAdapter(requestList, this);

        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        rvRequests.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Adoption Requests");
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

                        RequestModel request =
                                doc.toObject(RequestModel.class);

                        request.setRequestId(doc.getId());

                        requestList.add(request);
                    }

                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                })
                .addOnFailureListener(e -> {

                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(
                            ViewRequestsActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    private void updateEmptyState() {
        if (requestList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public void onApprove(RequestModel request) {
        updateRequestStatus(request.getRequestId(), "Approved");
    }

    @Override
    public void onReject(RequestModel request) {
        updateRequestStatus(request.getRequestId(), "Rejected");
    }

    private void updateRequestStatus(String requestId, String newStatus) {
        db.collection("adoptionRequests").document(requestId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request " + newStatus, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
