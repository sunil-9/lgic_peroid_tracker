package com.example.periodtracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.example.periodtracker.model.UserModel;
import com.example.periodtracker.utils.Constants;
import com.example.periodtracker.utils.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.periodtracker.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static com.example.periodtracker.utils.Constants.PLEASE_WAIT;

public class SignupActivity extends AppCompatActivity{
    private MaterialEditText userName;
    private MaterialEditText emailAddress;
    private MaterialEditText password;
    MaterialEditText et_period_length;
    MaterialEditText et_period_cycle;
    private RadioGroup radioGroup;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    TextView Last_period_date;
    String last_date_year,last_date_month,last_date_day;
    private int mYear, mMonth, mDay;
    DatePickerDialog dialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpToolbar();
        progressBar= findViewById(R.id.progressBar);

        //for progress dialog
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage(PLEASE_WAIT);
        progressDialog.setCanceledOnTouchOutside(false);


        //creating an instance of firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        userName= findViewById(R.id.userName);
        emailAddress=findViewById(R.id.email);
        password = findViewById(R.id.password);
         et_period_cycle = findViewById(R.id.period_cycle);
         et_period_length = findViewById(R.id.period_length);
        Last_period_date = findViewById(R.id.last_period_date);
        radioGroup= findViewById(R.id.radioButton);
        Button registerBtn = findViewById(R.id.register);


        Last_period_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                last_date_day = ""+dayOfMonth;
                                last_date_month = ""+monthOfYear;
                                last_date_year = ""+year;
                                Last_period_date.setText("Year: "+last_date_year+"month :" +last_date_month+"day :"+last_date_day);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });
        //when the register button is clicked these function will run
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 progressDialog.show();

                // the values inserted in the field are extracted and saved in varaible String
                final String user_name= userName.getText().toString();
                final String email = emailAddress.getText().toString();
                final String txt_password=password.getText().toString();
                final String period_cycle=et_period_length.getText().toString().trim();
                final String period_length=et_period_cycle.getText().toString();
                int checkedId=radioGroup.getCheckedRadioButtonId();
                RadioButton terms = radioGroup.findViewById(checkedId);
                if(terms == null){
                    //if no gender is slected this toast is pops up
                    Toast.makeText(SignupActivity.this, "Accept terms and conditions", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss(); }
                else{
                    if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password)
                            || TextUtils.isEmpty(period_cycle) || TextUtils.isEmpty(period_length)|| TextUtils.isEmpty(last_date_year)){
                        //is anyone field is left blank toast pops up
                        Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
//                         progressDialog.dismiss();
                    }
                    else{

                        register(user_name, email, txt_password,period_cycle,period_length,last_date_day,last_date_month,last_date_year);
//                    progressDialog.dismiss();
                    }

                }
            }
        });
    }
    private void setUpToolbar() {
        Toolbar toolbars = findViewById(R.id.toolbar);
        setSupportActionBar(toolbars);

        getSupportActionBar().setTitle("Register");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbars.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
        //end of the back arrow navigation code in register

    }

    // register() method passes the information to firebase
    private void register(final String user_name, final String email,  final String txt_password, final String txt_period_cycle,final String txt_period_length, String last_date_day,String last_date_month,String last_date_year) {
//        progressBar.setVisibility(View.VISIBLE);
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser rUser= firebaseAuth.getCurrentUser();
                    String userId = rUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance(Constants.DB_URL).getReference("Users").child(userId);
                    UserModel userModel = new UserModel(userId,user_name,email,txt_period_cycle,txt_period_length,last_date_day,
                            last_date_month,last_date_year,"default");

                    databaseReference.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            progressBar.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            if(task.isSuccessful()) {
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}