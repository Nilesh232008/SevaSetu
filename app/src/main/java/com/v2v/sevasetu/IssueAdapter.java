package com.v2v.sevasetu;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder> {

    private List<IssueModel> issueList;
    private Context context;
    private FirebaseFirestore db;

    public IssueAdapter(List<IssueModel> issueList, Context context, FirebaseFirestore db) {
        this.issueList = issueList;
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public IssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_issue, parent, false);
        return new IssueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueViewHolder holder, int position) {
        IssueModel issue = issueList.get(position);

        holder.textCategory.setText("Category: " + issue.getCategory());
        holder.textDescription.setText(issue.getDescription());
        holder.textStatus.setText("Status: " + issue.getStatus());

        // Optional: Long click to delete
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Issue")
                    .setMessage("Are you sure you want to delete this issue?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        db.collection("issues").document(issue.getId()).delete()
                                .addOnSuccessListener(aVoid -> {
                                    issueList.remove(position);
                                    notifyItemRemoved(position);
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return issueList.size();
    }

    public static class IssueViewHolder extends RecyclerView.ViewHolder {

        TextView textCategory, textDescription, textStatus;

        public IssueViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.textCategory);
            textDescription = itemView.findViewById(R.id.textDescription);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
    }
}
