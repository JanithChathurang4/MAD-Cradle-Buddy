package com.example.infantcareaidingmobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogingActivity extends AppCompatActivity {

    private EditText luser,lpass;
    private Button lbtn;
    private TextView lShiftR;
    private FirebaseAuth Fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);

        luser = findViewById(R.id.LUsername);
        lpass = findViewById(R.id.LPassword);
        lbtn = findViewById(R.id.Lbtn);
        lShiftR = findViewById(R.id.LgotoReg);
        Fauth = FirebaseAuth.getInstance();

        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = luser.getText().toString().trim();
                String pw = lpass.getText().toString().trim();

                if(user.isEmpty() || pw.isEmpty()){
                    Toast.makeText(LogingActivity.this,"Fields are empty, Fill the all fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                Fauth.signInWithEmailAndPassword(user,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LogingActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ICAMAHome.class));
                        }
                        else{
                            Toast.makeText(LogingActivity.this,"Error Occured"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        lShiftR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Fauth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),ICAMAHome.class));
        }
    }
}