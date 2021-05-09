package com.example.infantcareaidingmobileapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ICAMAHome extends AppCompatActivity {
private Button Logout,infantSig,monthlyPhoto,records;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_c_a_m_a_home);

        Logout = findViewById(R.id.Hlogout);
        infantSig = findViewById(R.id.infntSbtn);
        monthlyPhoto = findViewById(R.id.MonthPhoto);
        records = findViewById(R.id.records);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LogingActivity.class));
                finish();
            }
        });
        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RecordActivity.class));
            }
        });

        monthlyPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMonthlyPhoto = new Intent(ICAMAHome.this,MonthlyPhoto.class);
                startActivity(toMonthlyPhoto);
            }
        });

        infantSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toInfantSigns = new Intent(ICAMAHome.this, InfantSignes.class);
                startActivity(toInfantSigns);
            }
        });
    }
}