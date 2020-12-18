package com.example.periodtracker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.periodtracker.R;
import com.example.periodtracker.utils.PrefManager;

import static com.example.periodtracker.utils.Constants.PERIOD_CYCLE;
import static com.example.periodtracker.utils.Constants.PERIOD_LENGTH;


public class HistoryFragment extends Fragment {
    View view;
    PrefManager prefManager;
    TextView period_length,period_cycle;
    String length,cycle;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_history_fragment, container, false);
         prefManager =new PrefManager(getActivity());
        period_cycle = view.findViewById(R.id.avg_cycle_length);
        period_length = view.findViewById(R.id.avg_period_length);
        length = prefManager.getPeriodlength(PERIOD_LENGTH);
        cycle = prefManager.getPeriodlength(PERIOD_CYCLE);
        period_cycle.setText(cycle);
        period_length.setText(length);

        return view;
    }
}