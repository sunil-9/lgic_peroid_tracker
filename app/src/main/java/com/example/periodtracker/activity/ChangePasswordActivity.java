package com.example.periodtracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.periodtracker.R;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputEditText cur_pass, new_pass, re_pass;
    private Button update_pw;
    private String txt_cur_pass, txt_new_pass, txt_re_pass;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setupUI();
        update_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_cur_pass = cur_pass.getText().toString();
                txt_new_pass = new_pass.getText().toString();
                txt_re_pass = re_pass.getText().toString();

                if (TextUtils.isEmpty(txt_cur_pass)) {
                    cur_pass.setError("Current password required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_new_pass)) {
                    new_pass.setError("new password required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_re_pass)) {
                    re_pass.setError("retype new password required!");
                    return;
                }
                if (txt_re_pass.equals(txt_new_pass)) {
                    ProgressDialog dialog = ProgressDialog.show(ChangePasswordActivity.this, "Loading", "Please wait...", true);

                    String email = user.getEmail();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, txt_cur_pass);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(txt_new_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangePasswordActivity.this, " password changed", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                } else {
                                                    Toast.makeText(ChangePasswordActivity.this, "Please Try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this, "Incorrect old password ", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(ChangePasswordActivity.this, "retype password do not match ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setupUI() {
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        cur_pass = findViewById(R.id.cur_pass);
        new_pass = findViewById(R.id.new_pass);
        re_pass = findViewById(R.id.re_new_pas);
        update_pw = findViewById(R.id.update_pw);
    }
}