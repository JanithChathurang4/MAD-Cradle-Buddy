package com.example.infantcareaidingmobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.Stopwatch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {

    Button add,start,stop,reset,view;
    Chronometer chronometer;
    long stopTime = 0;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog pDialog;
    TextView total_feed;
    double total = 0;
    String dateString;
    ArrayList<Feeding> feedings = new ArrayList<>();
    String timeString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        add = findViewById(R.id.add);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        reset = findViewById(R.id.reset);
        view = findViewById(R.id.view);
        chronometer = findViewById(R.id.chronometer);
        total_feed = findViewById(R.id.feed);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Feedings").child(FirebaseAuth.getInstance().getUid());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait...");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        dateString = dateFormat.format(date);
        timeString = timeFormat.format(date);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
                chronometer.start();
                start.setEnabled(false);
                start.setFocusable(false);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTime = chronometer.getBase() - SystemClock.elapsedRealtime();
                chronometer.stop();
                System.out.println(stopTime);
                start.setEnabled(true);
                start.setFocusable(true);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetWatch();
                start.setEnabled(true);
                start.setFocusable(true);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(stopTime != 0) {
                    pDialog.show();
                    Feeding feeding = new Feeding();
                    feeding.setDate(dateString);
                    feeding.setTime(timeString);
                    feeding.setPeriod(stopTime / -1000);
                    reference.child(reference.push().getKey()).setValue(feeding)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pDialog.dismiss();
                                    Toast.makeText(RecordActivity.this, "Successfully data added", Toast.LENGTH_SHORT).show();
                                    resetWatch();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pDialog.dismiss();
                                    Toast.makeText(RecordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(RecordActivity.this, "Please record the time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedings.size() == 0) {
                    Toast.makeText(RecordActivity.this, "No data to view", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(RecordActivity.this, FeedingsActivity.class);
                    final Gson gson = new Gson();
                    String feedJson = gson.toJson(feedings);
                    System.out.println(feedJson);
                    intent.putExtra("feedings", feedJson);
                    startActivity(intent);
                }
            }
        });
        calculate();
    }

    private void calculate() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total = 0;
                if(snapshot.exists()) {
                    feedings.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Feeding feeding = ds.getValue(Feeding.class);
                        feeding.setId(ds.getKey());
                        feedings.add(feeding);
                        if(feeding.getDate().equals(dateString)){
                            total += feeding.getPeriod();
                        }
                        System.out.println(feeding.getPeriod());
                    }
                }
                DecimalFormat df = new DecimalFormat("#.#");
                total_feed.setText(String.valueOf(df.format(total / 60.0)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void resetWatch(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        stopTime = 0;
        chronometer.stop();
    }
}