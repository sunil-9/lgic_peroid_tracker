package com.example.periodtracker.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.periodtracker.R;
import com.example.periodtracker.utils.PermissionUtils;
import com.example.periodtracker.utils.PrefManager;

import static com.example.periodtracker.utils.Constants.FIRST_NAME;
import static com.example.periodtracker.utils.Constants.FIRST_PHONE;
import static com.example.periodtracker.utils.Constants.SECOND_NAME;
import static com.example.periodtracker.utils.Constants.SECOND_PHONE;
import static com.example.periodtracker.utils.Constants.THIRD_NAME;
import static com.example.periodtracker.utils.Constants.THIRD_PHONE;

public class SafetyActivity extends AppCompatActivity {
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private LocationManager mLocationManagerGPS;
    private LocationListener mLocationListenerGPS;

    private LocationManager mLocationManagerNetwork;
    private LocationListener mLocationListenerNetwork;
    private String lon, lat, alt, txt_first_phone, txt_second_phone, txt_third_phone, msg;
    EditText firstName, firstPhone, secondName, secondPhone, thirdName, thirdPhone;
    private PrefManager prefManager;
    private Button save, send_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sefety);

        mLocationManagerGPS = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        prefManager = new PrefManager(this);
        firstName = findViewById(R.id.first_person_name);
        firstPhone = findViewById(R.id.first_person_phone);
        secondName = findViewById(R.id.second_person_name);
        secondPhone = findViewById(R.id.second_person_phone);
        thirdName = findViewById(R.id.third_person_name);
        thirdPhone = findViewById(R.id.third_person_phone);

        if (prefManager.getPrefValue(FIRST_NAME) != null)
            firstName.setText(prefManager.getPrefValue(FIRST_NAME));
        if (prefManager.getPrefValue(FIRST_PHONE) != null)
            firstPhone.setText(prefManager.getPrefValue(FIRST_PHONE));
        if (prefManager.getPrefValue(SECOND_NAME) != null)
            secondName.setText(prefManager.getPrefValue(SECOND_NAME));
        if (prefManager.getPrefValue(SECOND_PHONE) != null)
            secondPhone.setText(prefManager.getPrefValue(SECOND_PHONE));
        if (prefManager.getPrefValue(THIRD_NAME) != null)
            thirdName.setText(prefManager.getPrefValue(THIRD_NAME));
        if (prefManager.getPrefValue(THIRD_PHONE) != null)
            thirdPhone.setText(prefManager.getPrefValue(THIRD_PHONE));


        save = findViewById(R.id.save);
        send_msg = findViewById(R.id.send_msg);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().toString() != null)
                    prefManager.setPrefValue(FIRST_NAME, firstName.getText().toString());
                if (firstPhone.getText().toString() != null)
                    prefManager.setPrefValue(FIRST_PHONE, firstPhone.getText().toString());
                if (secondName.getText().toString() != null)
                    prefManager.setPrefValue(SECOND_NAME, secondName.getText().toString());
                if (secondPhone.getText().toString() != null)
                    prefManager.setPrefValue(SECOND_PHONE, secondPhone.getText().toString());
                if (thirdName.getText().toString() != null)
                    prefManager.setPrefValue(THIRD_NAME, thirdName.getText().toString());
                if (thirdPhone.getText().toString() != null)
                    prefManager.setPrefValue(THIRD_PHONE, thirdPhone.getText().toString());
            }
        });


        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prefManager.getPrefValue(FIRST_PHONE) != null)
                    txt_first_phone = prefManager.getPrefValue(FIRST_PHONE);

                if (prefManager.getPrefValue(SECOND_PHONE) != null)
                    txt_second_phone = prefManager.getPrefValue(SECOND_PHONE);

                if (prefManager.getPrefValue(THIRD_PHONE) != null)
                    txt_third_phone = prefManager.getPrefValue(THIRD_PHONE);
                getPositionGPS();
                getPositionNetwork();
                if (TextUtils.isEmpty(lon) || TextUtils.isEmpty(lat)) {
                    Toast.makeText(SafetyActivity.this, "Failed to send message no location found", Toast.LENGTH_SHORT).show();
                    return;
                }
                msg = "your relative is in trouble find detail lon: " + lon + " lat " + lat + " alt " + alt;

                Toast.makeText(SafetyActivity.this, "To: " + txt_first_phone + " " + txt_second_phone + " " + txt_third_phone + " " + "message:" + msg, Toast.LENGTH_SHORT).show();

                try {
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(txt_first_phone, null, msg, null, null);
                    Toast.makeText(SafetyActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(SafetyActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getPositionGPS() {

        mLocationListenerGPS = new LocationListener() {

            public void onLocationChanged(@NonNull Location location) {
                lon = "" + location.getLongitude();
                lat = "" + location.getLatitude();
                alt = "" + location.getAltitude();
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                showAlert(R.string.GPS_disabled);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
//                btnGPS.setEnabled(false);
                mLocationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, mLocationListenerGPS);
            }
        }
    }

    private void getPositionNetwork() {
        mLocationManagerNetwork = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mLocationListenerNetwork = new LocationListener() {
            public void onLocationChanged(Location location) {

                lon = "" + location.getLongitude();
                lat = "" + location.getLatitude();
                alt = "" + location.getAltitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                showAlert(R.string.Network_disabled);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
//                btnNetwork.setEnabled(false);
//                mLocationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 0, mLocationListenerNetwork);
            }
        }
    }

    private void showAlert(int messageId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId).setCancelable(false).setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.GPS_permissions).setCancelable(false).setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(SafetyActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }).show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.GPS_permissions).setCancelable(false).setPositiveButton(R.string.btn_watch_permissions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enableMyLocation();
                    dialog.cancel();
//                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
                }
            }).setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManagerGPS != null) {
            mLocationManagerGPS.removeUpdates(mLocationListenerGPS);
        }

        if (mLocationManagerNetwork != null) {
            mLocationManagerNetwork.removeUpdates(mLocationListenerNetwork);
        }

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "request success", Toast.LENGTH_SHORT).show();
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
