package com.example.infantcareaidingmobileapplication;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText RUsername,RPassword,RbabyName,RBabyDOB;
    private Button Reg;
    private TextView ShiftL;
    RadioButton rdMale,rdFemale;
    ProgressBar pb;
    DatabaseReference DBref;
   // FirebaseDatabase fbDB;
    private FirebaseAuth fbAuth;
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        RUsername = findViewById(R.id.RUsername);
        RPassword = findViewById(R.id.RPassword);
        RbabyName = findViewById(R.id.Bname);
        RBabyDOB = findViewById(R.id.BBoDate);
        Reg = findViewById(R.id.Rbtn);
        rdMale = findViewById(R.id.male);
        rdFemale = findViewById(R.id.female);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);


        DBref = FirebaseDatabase.getInstance().getReference("Baby");
        fbAuth = FirebaseAuth.getInstance();
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputname = RUsername.getText().toString();
                String inputpw = RPassword.getText().toString();
                final String Bname = RbabyName.getText().toString();
                final String Bdob = RBabyDOB.getText().toString();

                if (rdMale.isChecked()) {
                    gender = "Male";
                }
                if (rdFemale.isChecked()) {
                    gender = "Female";
                }

                if(inputname.isEmpty() || inputpw.isEmpty() || Bname.isEmpty() || Bdob.isEmpty() ){
                    Toast.makeText(RegistrationActivity.this,"Fields are empty, Fill the all fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                fbAuth.createUserWithEmailAndPassword(inputname,inputpw)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pb.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    BabyUser nbu = new BabyUser(Bname,Bdob,gender);

                                    FirebaseDatabase.getInstance().getReference("Baby").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(nbu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(RegistrationActivity.this,"Registration Complete",Toast.LENGTH_SHORT);
                                            startActivity(new Intent(getApplicationContext(),LogingActivity.class));
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegistrationActivity.this,"මොන මගුලක් හරි වෙල",Toast.LENGTH_SHORT);
                                }

                                // ...
                            }
                        });
            }

        });
        /*ShiftL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogingActivity.class));
            }
        });*/
    }

    /*
   private EditText RUsername,RPassword,RbabyName,RBabyDOB;
   private Button Reg;
   private TextView ShiftL;
   private FirebaseAuth fAuth;
   FirebaseDatabase firebaseDB;
   DatabaseReference DBreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        RUsername = findViewById(R.id.RUsername);
        RPassword = findViewById(R.id.RPassword);
        RbabyName = findViewById(R.id.Bname);
        RBabyDOB = findViewById(R.id.BBoDate);
        Reg = findViewById(R.id.Rbtn);
        ShiftL = findViewById(R.id.RgoLoginText);
        fAuth = FirebaseAuth.getInstance();

        DBreference = FirebaseDatabase.getInstance().getReference("Baby");

        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),ICAMAHome.class));
            finish();
        }

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputname = RUsername.getText().toString();
                String inputpw = RPassword.getText().toString();
                final String Bname = RbabyName.getText().toString();
                final String Bdob = RBabyDOB.getText().toString();


                if(inputname.isEmpty() || inputpw.isEmpty() || Bname.isEmpty() || Bdob.isEmpty() ){
                    Toast.makeText(RegistrationActivity.this,"Fields are empty, Fill the all fields",Toast.LENGTH_SHORT).show();
                    return;
                }




                fAuth.createUserWithEmailAndPassword(inputname,inputpw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                            BabyUser nbu = new BabyUser(Bname,Bdob);
                            //String id = FirebaseDatabase.getInstance().getReference("Baby").push().getKey();
                            Toast.makeText(RegistrationActivity.this,"User created",Toast.LENGTH_SHORT).show();
                             FirebaseDatabase.getInstance().getReference("Baby").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(nbu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegistrationActivity.this,"User created",Toast.LENGTH_SHORT).show();
                                        System.out.println("අවුලක් නැ");
                                    }else{
                                        Toast.makeText(RegistrationActivity.this,"මොන මගුලක් හරි වෙල ",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }      
                        else{
                            Toast.makeText(RegistrationActivity.this,"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                }

        });
        ShiftL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogingActivity.class));
            }
        });
    }

 */
}