package com.example.periodtracker.fragments;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.example.periodtracker.R;
import com.example.periodtracker.utils.MyEventDay;
import com.example.periodtracker.utils.PrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import pl.rafman.scrollcalendar.ScrollCalendar;
//import pl.rafman.scrollcalendar.contract.DateWatcher;
//import pl.rafman.scrollcalendar.contract.MonthScrollListener;
//import pl.rafman.scrollcalendar.contract.OnDateClickListener;
//import pl.rafman.scrollcalendar.contract.State;
//import pl.rafman.scrollcalendar.data.CalendarDay;

import static com.example.periodtracker.utils.Constants.ASK_FOR_PERIOD;
import static com.example.periodtracker.utils.Constants.ASK_FOR_PERIOD_REMOVE;
import static com.example.periodtracker.utils.Constants.LAST_DATE_DAY;
import static com.example.periodtracker.utils.Constants.LAST_DATE_MONTH;
import static com.example.periodtracker.utils.Constants.LAST_DATE_YEAR;

public class CalendarFragment extends Fragment {
    private  View view;
    private List<EventDay> events = new ArrayList<>();
    PrefManager prefManager;

    public CalendarFragment() {
        // Required empty public constructor
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        prefManager = new PrefManager(getActivity());
//        List<EventDay> mEventDays = new ArrayList<>();
//        CalendarView mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
//        Calendar day =Calendar.getInstance();
//        events.add(new EventDay(day,R.drawable.pink_border));

        //for showing last period date.
        TextView last_period_date = view.findViewById(R.id.tv_lastDate);
        String lastPeriodDate =prefManager.getPrefValue(LAST_DATE_YEAR)+"-"+prefManager.getPrefValue(LAST_DATE_MONTH)+"-"+ prefManager.getPrefValue(LAST_DATE_DAY);
        last_period_date.setText(lastPeriodDate);


        //TODO: Calender ko event haru track garni kasari????? :(

        return view;
    }

}