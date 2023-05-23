package com.tgt.triggertactics;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ScrimsActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");

    String teamName;

    ImageButton btnBack;
    Button btnCreateScrim;
    EditText editScrimDate, editScrimTime;
    LinearLayout linearScrimList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrims);

        teamName = getIntent().getStringExtra("teamName");

        btnBack = findViewById(R.id.btnBack9);
        btnBack.setOnClickListener(v -> finish());

        btnCreateScrim = findViewById(R.id.btnCreateScrim);
        editScrimDate = findViewById(R.id.editScrimDate);
        editScrimTime = findViewById(R.id.editScrimTime);
        linearScrimList = findViewById(R.id.linearScrimList);

        final Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int cHour = c.get(Calendar.HOUR_OF_DAY);
        int cMinute = c.get(Calendar.MINUTE);

        DatePickerDialog dateDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            editScrimDate.setText(year + "/" + (month + 1) + "/" + day);
        }, cYear, cMonth, cDayOfMonth);

        TimePickerDialog timeDialog = new TimePickerDialog(this, (timePicker, hour, minute) -> {
            editScrimTime.setText(hour + ":" + minute);
        }, cHour, cMinute, false);

        editScrimDate.setOnClickListener(l -> {
            dateDialog.show();
        });

        editScrimTime.setOnClickListener(l -> {
            timeDialog.show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        btnCreateScrim.setOnClickListener(l -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String date = editScrimDate.getText().toString();
            String time = editScrimTime.getText().toString();
            String datetime = date + " " + time;

            String newKey = database.getReference("scrims").push().getKey();
            HashMap<String, Object> values = new HashMap<>();
            values.put("datetime", datetime);
            values.put("team1", teamName);
            values.put("team2", "");
            database.getReference("scrims").child(newKey).setValue(values);
        });
        database.getReference("scrims").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearScrimList.removeAllViews();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String datetime = child.child("datetime").getValue().toString();
                    String team1 = child.child("team1").getValue().toString();
                    String team2 = child.child("team2").getValue().toString();

                    View view = inflater.inflate(R.layout.layout_scrim, linearScrimList, false);
                    Button itemScrimName = view.findViewById(R.id.itemScrimName);
                    TextView itemScrimDate = view.findViewById(R.id.itemScrimDate);
                    Button btnScrimAction = view.findViewById(R.id.btnScrimAction);

                    if (team2.isEmpty()) {
                        itemScrimName.setText(team1);
                    } else {
                        itemScrimName.setText(team1 + " vs " + team2);
                    }
                    itemScrimDate.setText(datetime);
                    if (team2.isEmpty() && !teamName.equals(team1)) {
                        btnScrimAction.setText("Join");
                        btnScrimAction.setOnClickListener(l -> database.getReference("scrims").child(child.getKey()).child("team2").setValue(teamName));
                    } else {
                        btnScrimAction.setVisibility(View.GONE);
                    }
                    linearScrimList.addView(view);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}