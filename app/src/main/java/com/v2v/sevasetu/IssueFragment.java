package com.v2v.sevasetu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class IssueFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton addIssueFab;
    private FirebaseFirestore db;
    private List<IssueModel> issueList;
    private IssueAdapter adapter;

    public IssueFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue, container, false);

        recyclerView = view.findViewById(R.id.issueRecyclerView);
        addIssueFab = view.findViewById(R.id.addIssueFab);
        db = FirebaseFirestore.getInstance();

        issueList = new ArrayList<>();
        adapter = new IssueAdapter(issueList, requireContext(), db);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadIssues();

        addIssueFab.setOnClickListener(v -> showAddIssueDialog());

        return view;
    }

    private void loadIssues() {
        db.collection("issues").orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    issueList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        IssueModel issue = doc.toObject(IssueModel.class);
                        issue.setId(doc.getId());
                        issueList.add(issue);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddIssueDialog() {
        AddIssueDialog dialog = new AddIssueDialog(db, adapter, issueList);
        dialog.show(getParentFragmentManager(), "AddIssueDialog");
    }
}

