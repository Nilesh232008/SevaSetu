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

public class TimetableFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton addEventFab;
    private FirebaseFirestore db;
    private List<TimetableModel> eventList;
    private TimetableAdapter adapter;

    public TimetableFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        recyclerView = view.findViewById(R.id.timetableRecyclerView);
        addEventFab = view.findViewById(R.id.addEventFab);

        db = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();
        adapter = new TimetableAdapter(eventList, getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadEvents();

        addEventFab.setOnClickListener(v -> {
            AddTimetableDialog dialog = new AddTimetableDialog(db, adapter, eventList);
            dialog.show(getParentFragmentManager(), "AddTimetableDialog");
        });

        return view;
    }

    private void loadEvents() {
        db.collection("timetable")
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(snapshot -> {
                    eventList.clear();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        TimetableModel event = doc.toObject(TimetableModel.class);
                        if (event != null) {
                            event.setId(doc.getId());
                            eventList.add(event);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}

