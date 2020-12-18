package com.example.periodtracker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.periodtracker.model.UserModel;
import com.example.periodtracker.utils.Constants;
import com.example.periodtracker.utils.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.example.periodtracker.R;

import static com.example.periodtracker.utils.Constants.*;

public class LoginActivity extends AppCompatActivity {
    private MaterialEditText email, password;
    private TextView forgetPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabase;

    private Button loginBtn;
    private Button registerBtn;
    ProgressDialog progressDialog;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefManager = new PrefManager(this);
         loginBtn = findViewById(R.id.login);
         registerBtn= findViewById(R.id.register);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        forgetPassword=findViewById(R.id.forgotPassword);
        firebaseAuth= FirebaseAuth.getInstance();

        //for progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(PLEASE_WAIT);
        progressDialog.setCanceledOnTouchOutside(false);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email= email.getText().toString();
                String txt_password = password.getText().toString();
                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "All Fields Required",Toast.LENGTH_SHORT).show();
                }
                else{
                    login(txt_email, txt_password);
                }
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    private void login(String txt_email, String txt_password) {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser mUser= firebaseAuth.getCurrentUser();
                    String userId = mUser.getUid();
                    Query q  = FirebaseDatabase.getInstance(DB_URL).getReference("Users").child(userId);
                    q.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserModel user = snapshot.getValue(UserModel.class);
                            Log.e("TAG", "onDataChange: login activity data "+user.getEmail());
                            Toast.makeText(LoginActivity.this, ""+user.getLength(), Toast.LENGTH_SHORT).show();
                            //shared pref
                            prefManager.setPrefValue(LAST_DATE_DAY, user.getLast_date_day());
                            prefManager.setPrefValue(LAST_DATE_MONTH, user.getLast_date_month());
                            prefManager.setPrefValue(LAST_DATE_YEAR, user.getLast_date_year());
                            prefManager.setPrefValue(PERIOD_CYCLE, user.getCycle());
                            prefManager.setPrefValue(PERIOD_LENGTH, user.getLength());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}