package com.example.periodtracker.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.periodtracker.R;
import com.example.periodtracker.model.UserModel;
import com.example.periodtracker.utils.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.periodtracker.utils.Constants.DB_URL;
import static com.example.periodtracker.utils.Constants.PERIOD_CYCLE;
import static com.example.periodtracker.utils.Constants.PERIOD_LENGTH;
import static com.example.periodtracker.utils.Constants.PLEASE_WAIT;

public class SettingActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout peroid_cycle, peroid_length, change_pw, change_email,change_username;
    PrefManager prefManager;
    FirebaseUser user;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    //firebase
    private DatabaseReference mdatabase;
    private FirebaseAuth mauth;
    Query q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //for toolbar
        toolbar = findViewById(R.id.taskbar_common);
        toolbar.setTitle("Setting");
        prefManager = new PrefManager(this);
        setSupportActionBar(toolbar);
        peroid_cycle = findViewById(R.id.peroid_cycle);
        peroid_length = findViewById(R.id.peroid_length);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //for progress dialog
        progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setMessage(PLEASE_WAIT);
        progressDialog.setCanceledOnTouchOutside(false);

        //change  email and password
        change_email = findViewById(R.id.change_email);
        change_pw = findViewById(R.id.change_pw);
        change_username=findViewById(R.id.change_username);
        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeData("Change Email Address","enter username here",true);

            }
        });
        change_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeData("Change User Name","enter username here",false);
            }
        });
        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startActivity(new Intent(SettingActivity.this,ChangePasswordActivity.class));
            }
        });

        //firebase
        mauth = FirebaseAuth.getInstance();
        FirebaseUser muser = mauth.getCurrentUser();
        q = FirebaseDatabase.getInstance(DB_URL).getReference("Users").child(mauth.getUid());
        List<String> details = getPeroidDetailsFirebase();
        peroid_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(SettingActivity.this);
                LayoutInflater inflater = LayoutInflater.from(SettingActivity.this);
                View myView = inflater.inflate(R.layout.update_length, null);
                myDialog.setView(myView);
                final AlertDialog dialog = myDialog.create();
                final TextView title = myView.findViewById(R.id.title);
                final EditText input_field = myView.findViewById(R.id.input_field);
                input_field.setText(details.get(1));
                Button ok_btn = myView.findViewById(R.id.ok);
                title.setText(R.string.peroid_cycle);
                dialog.show();
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txt_length = input_field.getText().toString().trim();
                        if (TextUtils.isEmpty(txt_length)) {
                            input_field.setError("Note is empty");
                            return;
                        }
                        progressDialog.show();
                        String input_text = input_field.getText().toString().trim();
                        Toast.makeText(SettingActivity.this, "length is " + input_field.getText().toString(), Toast.LENGTH_SHORT).show();
                        prefManager.setPeriodCycle(PERIOD_CYCLE, input_text);
                        setPeroidDetailsFirebase("cycle", input_text);
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }
                });

            }
        });
        peroid_length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(SettingActivity.this);
                LayoutInflater inflater = LayoutInflater.from(SettingActivity.this);
                View myView = inflater.inflate(R.layout.update_length, null);
                myDialog.setView(myView);
                final AlertDialog dialog = myDialog.create();
                final TextView title = myView.findViewById(R.id.title);
                final EditText input_field = myView.findViewById(R.id.input_field);
                input_field.setText(details.get(0));
                Button ok_btn = myView.findViewById(R.id.ok);
                title.setText(R.string.peroid_length);
                dialog.show();
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input_text = input_field.getText().toString().trim();
                        progressDialog.show();
                        Toast.makeText(SettingActivity.this, "length is " + input_field.getText().toString(), Toast.LENGTH_SHORT).show();
                        prefManager.setPeriodCycle(PERIOD_LENGTH, input_text);
                        setPeroidDetailsFirebase("length", input_text);
                        progressDialog.dismiss();
                        dialog.dismiss();

                    }
                });
            }
        });


    }

  private void changeData(String title_text,String hint,boolean changeEmailId) {
      AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
      LayoutInflater inflater = LayoutInflater.from(this);
      View myView = inflater.inflate(R.layout.name_popup, null);
      myDialog.setView(myView);
      final AlertDialog dialog = myDialog.create();
      final TextView title = myView.findViewById(R.id.title);
      final EditText input_field = myView.findViewById(R.id.input_field);
      title.setText(title_text);
      input_field.setHint(hint);
      Button btn_save = myView.findViewById(R.id.btn_save);
      HashMap<String, Object> map = new HashMap<>();
      dialog.show();
      btn_save.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String mdata = input_field.getText().toString().trim();
              if (TextUtils.isEmpty(mdata)) {
                  input_field.setError("input field is empty");
                  return;
              }
              if(changeEmailId) {
                  updateUserEmail(mdata);
                  map.put("email", mdata);
              }
              else {
                  map.put("username", mdata);
              }




              FirebaseDatabase.getInstance(DB_URL).getReference().child("Users").child(user.getUid()).updateChildren(map);
              Toast.makeText(SettingActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
              dialog.dismiss();
          }
      });

  }

    public void setPeroidDetailsFirebase(String field, String input_text) {

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                snapshot.getRef().child(field).setValue(input_text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public List<String> getPeroidDetailsFirebase() {
        List<String> returnStrings = new ArrayList<String>();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                returnStrings.add(user.getLength());
                returnStrings.add(user.getCycle());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return returnStrings;
    }

    private void updateUserEmail(String email) {
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "successful updated email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}