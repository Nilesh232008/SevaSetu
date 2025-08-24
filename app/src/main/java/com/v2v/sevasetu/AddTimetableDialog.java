package com.v2v.sevasetu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

public class AddTimetableDialog extends DialogFragment {

    private FirebaseFirestore db;
    private TimetableAdapter adapter;
    private List<TimetableModel> eventList;

    public AddTimetableDialog(FirebaseFirestore db, TimetableAdapter adapter, List<TimetableModel> eventList) {
        this.db = db;
        this.adapter = adapter;
        this.eventList = eventList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_event, container, false);

        EditText titleEdit = view.findViewById(R.id.editEventTitle);
        EditText descEdit = view.findViewById(R.id.editEventDescription);
        Button pickDateBtn = view.findViewById(R.id.btnPickDate);
        Button submitBtn = view.findViewById(R.id.btnAddEvent);

        Calendar calendar = Calendar.getInstance();

        pickDateBtn.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                (view2, hour, minute) -> {
                                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                                    calendar.set(Calendar.MINUTE, minute);
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE), true);
                        timePickerDialog.show();
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        submitBtn.setOnClickListener(v -> {
            String title = titleEdit.getText().toString().trim();
            String desc = descEdit.getText().toString().trim();

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(getContext(), "All fields required", Toast.LENGTH_SHORT).show();
                return;
            }

            TimetableModel event = new TimetableModel(title, desc, calendar.getTimeInMillis());
            db.collection("timetable").add(event).addOnSuccessListener(ref -> {
                event.setId(ref.getId());
                eventList.add(event);
                adapter.notifyItemInserted(eventList.size() - 1);
                dismiss();
            });
        });

        return view;
    }
}
