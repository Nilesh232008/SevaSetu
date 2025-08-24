package com.v2v.sevasetu;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class ReminderFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton addReminderFab;
    private ReminderAdapter adapter;
    private ArrayList<ReminderModel> reminderList;

    public ReminderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        recyclerView = view.findViewById(R.id.reminderRecyclerView);
        addReminderFab = view.findViewById(R.id.addReminderFab);

        reminderList = new ArrayList<>();
        adapter = new ReminderAdapter(reminderList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadReminders(); // Optionally from SharedPreferences or SQLite

        addReminderFab.setOnClickListener(v -> showAddReminderDialog());

        return view;
    }

    private void showAddReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_reminder, null);
        builder.setView(dialogView);

        EditText editTitle = dialogView.findViewById(R.id.editReminderTitle);
        Button btnPickTime = dialogView.findViewById(R.id.btnPickTime);
        Button btnSetReminder = dialogView.findViewById(R.id.btnSetReminder);

        final Calendar calendar = Calendar.getInstance();

        btnPickTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (view1, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        Toast.makeText(getContext(), "Time set", Toast.LENGTH_SHORT).show();
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        AlertDialog dialog = builder.create();

        btnSetReminder.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            if (title.isEmpty()) {
                editTitle.setError("Enter title");
                return;
            }

            long timeInMillis = calendar.getTimeInMillis();

            ReminderModel reminder = new ReminderModel(title, timeInMillis);
            reminderList.add(reminder);
            adapter.notifyItemInserted(reminderList.size() - 1);

            setAlarm(reminder);

            dialog.dismiss();
        });

        dialog.show();
    }

    private void setAlarm(ReminderModel reminder) {
        Intent intent = new Intent(getContext(), ReminderReceiver.class);
        intent.putExtra("reminderTitle", reminder.getTitle());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), (int) System.currentTimeMillis(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.getTime(), pendingIntent);
    }

    private void loadReminders() {
        // For hackathon, skip DB — use in-memory list only
    }
}

