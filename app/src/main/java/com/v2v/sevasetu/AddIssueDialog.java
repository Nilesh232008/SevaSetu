package com.v2v.sevasetu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AddIssueDialog extends DialogFragment {

    private FirebaseFirestore db;
    private IssueAdapter adapter;
    private List<IssueModel> issueList;

    public AddIssueDialog(FirebaseFirestore db, IssueAdapter adapter, List<IssueModel> issueList) {
        this.db = db;
        this.adapter = adapter;
        this.issueList = issueList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_issue, container, false);

        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        EditText editDescription = view.findViewById(R.id.editDescription);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        String[] categories = {"Water", "Electricity", "Lift", "Security", "Cleanliness"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(spinnerAdapter);

        btnSubmit.setOnClickListener(v -> {
            String category = spinnerCategory.getSelectedItem().toString();
            String description = editDescription.getText().toString().trim();

            if (description.isEmpty()) {
                editDescription.setError("Description required");
                return;
            }

            IssueModel newIssue = new IssueModel(category, description, "Pending", System.currentTimeMillis());

            db.collection("issues")
                    .add(newIssue)
                    .addOnSuccessListener(documentReference -> {
                        newIssue.setId(documentReference.getId());
                        issueList.add(0, newIssue); // add to top
                        adapter.notifyItemInserted(0);
                        dismiss();
                    });
        });

        return view;
    }
}

