package com.example.periodtracker.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.periodtracker.R;
import com.example.periodtracker.model.HistoryModel;
import com.example.periodtracker.utils.Constants;
import com.example.periodtracker.utils.PrefManager;
import com.example.periodtracker.utils.Utils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.periodtracker.utils.Constants.LAST_DATE_DAY;
import static com.example.periodtracker.utils.Constants.LAST_DATE_MONTH;
import static com.example.periodtracker.utils.Constants.LAST_DATE_YEAR;
import static com.example.periodtracker.utils.Constants.MILLISECOND_IN_ONE_DAY;
import static com.example.periodtracker.utils.Constants.PERIOD_CYCLE;
import static com.example.periodtracker.utils.Constants.PERIOD_LENGTH;

public class CalendarFragment extends Fragment {
    private View view;
    CompactCalendarView calendarView;
    TextView showMonth, tv_nextDate;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private FirebaseAuth firebaseAuth;

    ArrayList<Long> list = new ArrayList<Long>();
    AlertDialog.Builder builder;
    private DatabaseReference databaseReference;
    private Calendar calendar;
    PrefManager prefManager;

    String userId;

    public CalendarFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        prefManager = new PrefManager(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        tv_nextDate = view.findViewById(R.id.tv_nextDate);

        //for showing last period date.
        TextView last_period_date = view.findViewById(R.id.tv_lastDate);

        int months = Integer.parseInt(prefManager.getPrefValue(LAST_DATE_MONTH)) + 1;
        String lastPeriodDate = prefManager.getPrefValue(LAST_DATE_YEAR) + "-" + months + "-" + prefManager.getPrefValue(LAST_DATE_DAY);
        last_period_date.setText(lastPeriodDate);
        builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        calendarView = view.findViewById(R.id.compactcalendar_view);
        showMonth = view.findViewById(R.id.showSrollMonth);

        //First Day of Week
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        calendarView.setUseThreeLetterAbbreviation(true);
        calendarView.shouldSelectFirstDayOfMonthOnScroll(false);
        calendarView.displayOtherMonthDays(false);
        calendarView.setCurrentSelectedDayTextColor(R.color.text_color);
        showMonth.setText("Slide the calender");
        calendarView.removeAllEvents();

//       calendarView.
        FirebaseUser rUser = firebaseAuth.getCurrentUser();
        userId = rUser.getUid();
        getHistoryDate();
        showLastDate();
        showPredictDate();

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Boolean clicked = false;
                for (int i = 0; i < list.size(); i++) {
                    if (dateClicked.getTime() == list.get(i)) {
                        clicked = true;
                        break;
                    } else {
                        clicked = false;
                    }
                }
                if (clicked) {
//                    addDataToList(dateClicked);
                    showDialogOnDayClicked("Remove period?", "do you want to remove this date as period?", dateClicked, false);
                } else {
//                    removeDataToList(dateClicked);
                    showDialogOnDayClicked("add period?", "do you want to add this date as period?", dateClicked, true);
                }
                updateEvent();
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                showMonth.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
        updateEvent();
        return view;
    }

    private void getHistoryDate() {

        // Get a reference to our posts
        databaseReference = FirebaseDatabase.getInstance(Constants.DB_URL).getReference().child("history").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HistoryModel historyModel = dataSnapshot1.getValue(HistoryModel.class);
                    Log.e("TAG", "testing history " + historyModel.getStartTime());
                    list.add(historyModel.getStartTime());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showPredictDate() {

        int length = Integer.parseInt(prefManager.getPrefValue(PERIOD_LENGTH));
        int cycle = Integer.parseInt(prefManager.getPrefValue(PERIOD_CYCLE));
        Long c = (long) cycle;
        Long nextStart = prefManager.getPrefLastPeriodTime() + (c * MILLISECOND_IN_ONE_DAY);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nextStart);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        tv_nextDate.setText("" + mYear + "-" + mMonth + "-" + mDay);

        for (int i = 0; i < length; i++) {
            list.add(nextStart + (MILLISECOND_IN_ONE_DAY * i));
            Log.e("TAG", "showPredictDate: " + (nextStart + (MILLISECOND_IN_ONE_DAY * i)));
        }
    }

    private void showLastDate() {
        int length = Integer.parseInt(prefManager.getPrefValue(PERIOD_LENGTH));
        for (int i = 0; i < length; i++) {
            list.add(prefManager.getPrefLastPeriodTime() + (MILLISECOND_IN_ONE_DAY * (i)));
            Log.e("TAG", "showPredictDate: " + (prefManager.getPrefLastPeriodTime() + (MILLISECOND_IN_ONE_DAY * (i))));
        }
    }


    private void showDialogOnDayClicked(String dialog_title, String dialog_msg, Date dateClicked, boolean add) {
        builder.setMessage(dialog_msg).setTitle(dialog_title);
        builder.setMessage(dialog_msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (add) {
                            addDataToList(dateClicked);
                            dialog.dismiss();
                        } else {
                            removeDataToList(dateClicked);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(getActivity(), "you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(dialog_title);
        alert.show();
    }

    private void removeDataToList(Date dateClicked) {
        databaseReference = FirebaseDatabase.getInstance(Constants.DB_URL).getReference("history");
        String h_id = databaseReference.push().getKey();
        String milisec = "" + dateClicked.getTime();
        HistoryModel historyModel = new HistoryModel(h_id, dateClicked.toString(), prefManager.getPeriodlength(PERIOD_LENGTH), userId, dateClicked.getTime());
        databaseReference.child(userId).child(milisec).removeValue();
        list.remove(dateClicked.getTime());
        updateEvent();
    }

    private void addDataToList(Date dateClicked) {
        databaseReference = FirebaseDatabase.getInstance(Constants.DB_URL).getReference().child("history");
        String h_id = databaseReference.push().getKey();
        Long temp;
        HistoryModel historyModel;
        int length = Integer.parseInt(prefManager.getPrefValue(PERIOD_LENGTH));
        for (int i = 0; i < length; i++) {
            temp = dateClicked.getTime() + (MILLISECOND_IN_ONE_DAY * (i));
            list.add(temp);
            Log.e("TAG", "checked: " + temp);
            historyModel = new HistoryModel(h_id, dateClicked.toString(), prefManager.getPeriodlength(PERIOD_LENGTH), userId, temp);
            databaseReference.child(userId).child("" + temp).setValue(historyModel)
                    .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                                              }
                                          }
                    );
        }
        updateEvent();
    }

    private void updateEvent() {
        Log.e("TAG", "updateEvent: the data is updated : old data :" + list);
        ArrayList<Long>
                newList = Utils.removeDuplicates(list);
        calendarView.removeAllEvents();
        list =newList;
        Log.e("TAG", "updateEvent: the data is updated : new data :" + newList);

        List<Event> events = new ArrayList();
        Event ev1;
        for (int i = 0; i < newList.size(); i++) {

            ev1 = new Event(R.color.pink, newList.get(i), "period date");
            events.add(ev1);

        }
        calendarView.addEvents(events);

        Log.e("TAG", "updateEvent: the data is updated : finished");

    }

}