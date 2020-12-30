package com.example.periodtracker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.periodtracker.R;
import com.example.periodtracker.adapter.DisplayHistory;
import com.example.periodtracker.model.HistoryModel;
import com.example.periodtracker.utils.PrefManager;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.example.periodtracker.utils.Constants.PERIOD_CYCLE;
import static com.example.periodtracker.utils.Constants.PERIOD_LENGTH;


public class HistoryFragment extends Fragment {
    View view;
    PrefManager prefManager;
    TextView period_length,period_cycle;
    String length,cycle, userId;

//    creating object of recycle view
    RecyclerView previous_cycles;
    DisplayHistory adapter;

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("history").child(userId);
        Query queries = ref.orderByChild("startTime");
        previous_cycles = view.findViewById(R.id.previous_cycles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        previous_cycles.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<HistoryModel> options
                = new FirebaseRecyclerOptions.Builder<HistoryModel>()
                .setQuery(queries, HistoryModel.class)
                .build();

        adapter = new DisplayHistory( options,getActivity());
        previous_cycles.setAdapter(adapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }

    }
}