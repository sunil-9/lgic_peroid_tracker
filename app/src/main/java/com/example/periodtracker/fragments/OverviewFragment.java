package com.example.periodtracker.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.periodtracker.R;
import com.example.periodtracker.utils.PrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.periodtracker.utils.Constants.LAST_DATE_DAY;
import static com.example.periodtracker.utils.Constants.LAST_DATE_MONTH;
import static com.example.periodtracker.utils.Constants.LAST_DATE_YEAR;
import static com.example.periodtracker.utils.Constants.PERIOD_CYCLE;
import static com.example.periodtracker.utils.Constants.PERIOD_LENGTH;


public class OverviewFragment extends Fragment {
    View view;
    @Nullable
    private Calendar until;
    String last_day, last_month, last_year, cycle, length, today_day, today_month, today_year, remain;
    private TextView currentDate;

    PrefManager prefManager;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_overview, container, false);
        Button remainingDays = view.findViewById(R.id.remainingDays);
        currentDate =view.findViewById(R.id.currentDate);


        //get last period date
        prefManager = new PrefManager(getActivity());
        last_day = prefManager.getPrefValue(LAST_DATE_DAY);
        last_month = prefManager.getPrefValue(LAST_DATE_MONTH);
        last_year = prefManager.getPrefValue(LAST_DATE_YEAR);
        cycle = prefManager.getPrefValue(PERIOD_CYCLE);
        length = prefManager.getPrefValue(PERIOD_LENGTH);


        int int_last_day = Integer.parseInt(last_day);
        int int_last_month = Integer.parseInt(last_month);
        int int_last_year = Integer.parseInt(last_year);
        int int_cycle = Integer.parseInt(cycle);
        int int_length = Integer.parseInt(length);

        Log.e("TAG", "value of cycle: is "+int_cycle );
        Log.e("TAG", "value of int_length: is "+int_length );
        Log.e("TAG", "value of int_last_year: is "+int_last_year );
        Log.e("TAG", "value of int_last_month: is "+int_last_month );
        Log.e("TAG", "value of int_last_day: is "+int_last_day );

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int days = c.get(Calendar.DAY_OF_MONTH);
        currentDate.setText("Today is: "+year+"/"+month+"/"+days);
        Log.e("TAG", " new value of year: is "+year );
        Log.e("TAG", " new value of month: is "+month );
        Log.e("TAG", " new value of days: is "+days );

        if (int_last_year < year) {
            Toast.makeText(getActivity(), "last peroid in previous Year ", Toast.LENGTH_SHORT).show();

        } else if (int_last_month < month) {
            c.set(Calendar.MONTH, int_last_month);
            int numDays = c.getActualMaximum(Calendar.DATE);
            remain = ""+( int_cycle- ((numDays-int_last_day)));
            Log.e("TAG", " new value of remain: is "+( int_cycle- ((numDays-int_last_day))));

            //Todo: need to calculate here.
            Toast.makeText(getActivity(), "last peroid in previous month ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "last peroid in previous month ", Toast.LENGTH_SHORT).show();
            remain = (int_cycle - (days - int_last_day) ) + " days Remains";
            Log.e("TAG", " new value of remain: is "+(int_cycle - (days - int_last_day) ) );

        }
        remainingDays.setText(remain);
        return view;
    }

}